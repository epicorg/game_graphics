package epic.org;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import game.net.communication.ServerCommunicationThread;

/**
 * The user has to insert the address of the server he wants to communicate with.
 */
public class StartActivity extends AppCompatActivity {

    private static final String KEY_SERVER_ADDRESS = "serverAddress";

    private Activity activity;

    private EditText serverAddress;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        activity = this;
        Resources res = this.getResources();
        ServerCommunicationThread.getInstance().init(res.getInteger(R.integer.connectionWaitTime), res.getInteger(R.integer.serverPort));

        serverAddress = findViewById(R.id.start_server_address);
        Button serverAddressButton = findViewById(R.id.start_server_address_button);

        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        final SharedPreferences.Editor editor = preferences.edit();

        serverAddressButton.setOnClickListener(v -> {
            String serverAddressString = serverAddress.getText().toString().trim();
            ServerCommunicationThread.setServerAddress(serverAddressString);

            editor.putString(KEY_SERVER_ADDRESS, serverAddressString);
            editor.apply();

            Intent intent = new Intent(activity, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverAddress.setText(preferences.getString(KEY_SERVER_ADDRESS, ""));
    }

}
