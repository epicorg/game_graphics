package game.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import epic.org.R;
import game.audio.AudioCallManager;
import game.data.Room;
import game.data.Team;
import game.data.UserData;
import game.net.communication.JSONd;
import game.net.communication.NotConnectedException;
import game.net.communication.RequestMaker;
import game.net.communication.ServerCommunicationThread;
import game.net.fieldsnames.GameFields;
import game.net.fieldsnames.ServicesFields;
import game.player.Player;

/**
 * Represents a menu for change settings and exit during the game.
 *
 * @author Torlaschi
 * @date 12/05/2015
 * @see RequestMaker
 */
public class SettingsScreen {

    private static final String LOG_TAG = "SettingsScreen";

    private final ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private final Activity activity;
    private final LinearLayout container;
    private final RequestMaker requestMaker;

    private final SettingsScreen settingsScreen;

    /**
     * Creates a new <code>SettingsScreen</code> with a given Layout.
     *
     * @param activity     <code>Activity</code> on which thread the <code>SettingsScreen</code> should work
     * @param container    layout to which the <code>SettingsScreen</code> should be added
     * @param requestMaker <code>RequestMaker</code> used to send the exit request
     */
    public SettingsScreen(Activity activity, LinearLayout container, RequestMaker requestMaker) {
        this.activity = activity;
        this.container = container;
        this.requestMaker = requestMaker;
        settingsScreen = this;
        setup();
    }

    private void setup() {
        SettingsContainer settingsContainer = new SettingsContainer(activity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        settingsContainer.setLayoutParams(layoutParams);

        LinearLayout buttonsContainer = new LinearLayout(activity);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonsContainer.setLayoutParams(buttonLayoutParams);
        buttonsContainer.setOrientation(LinearLayout.HORIZONTAL);

        int padding = activity.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        settingsContainer.setPadding(padding, padding, padding, padding);
        settingsContainer.setOrientation(LinearLayout.VERTICAL);

        Button muteButton = activity.findViewById(R.id.game_menu_mute_unmute);
        Button quitButton = activity.findViewById(R.id.game_menu_quit);
        Button cancelButton = activity.findViewById(R.id.game_menu_cancel);

        muteButton.setOnClickListener(view -> {
            Toast.makeText(activity, R.string.messageMuting, Toast.LENGTH_SHORT).show();
            AudioCallManager.getInstance().muteUnMute();
        });

        quitButton.setOnClickListener(view -> {
            Toast.makeText(activity, R.string.messageQuitting, Toast.LENGTH_SHORT).show();
            try {
                serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(
                        new JSONd(ServicesFields.SERVICE, ServicesFields.GAME.toString()),
                        new JSONd(ServicesFields.SERVICE_TYPE, GameFields.GAME_STATUS.toString()),
                        new JSONd(GameFields.GAME_EXIT, true)));
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
        });

        cancelButton.setOnClickListener(view -> settingsScreen.hide());

        container.removeAllViews();
        buttonsContainer.addView(muteButton);
        buttonsContainer.addView(quitButton);
        settingsContainer.addView(buttonsContainer);
        settingsContainer.addView(cancelButton);

        ScrollView scrollView = new ScrollView(activity);
        scrollView.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        LinearLayout playersContainer = new LinearLayout(activity);
        LinearLayout.LayoutParams playersLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        playersContainer.setLayoutParams(playersLayout);
        playersContainer.setOrientation(LinearLayout.VERTICAL);

        Room room = (Room) UserData.DATA.getData(ServicesFields.CURRENT_ROOM);

        for (Team t : room.getTeams()) {
            TextView teamName = new TextView(activity);
            teamName.setText(t.getName());
            teamName.setTypeface(teamName.getTypeface(), Typeface.BOLD);

            playersContainer.addView(teamName);

            for (Player player : t.getPlayers()) {
                TextView playerName = new TextView(activity);
                playerName.setText(player.getName());
                playerName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                playersContainer.addView(playerName);
            }
        }

        scrollView.addView(playersContainer);
        settingsContainer.addView(scrollView);
        container.addView(settingsContainer);
    }

    public void show() {
        Log.d(LOG_TAG, "Show");
        activity.runOnUiThread(() -> {
            container.setVisibility(View.VISIBLE);
            container.bringToFront();
        });
    }

    public void hide() {
        Log.d(LOG_TAG, "Hide");
        container.setVisibility(View.GONE);
    }

    private static class SettingsContainer extends LinearLayout {

        public SettingsContainer(Context context) {
            super(context);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }

    }

}
