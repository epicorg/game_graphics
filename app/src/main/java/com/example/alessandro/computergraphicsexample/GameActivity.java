package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import game.GameManager;
import game.Room;
import game.codes.TextureCodes;
import game.graphics.Map;
import game.graphics.Obstacle;
import game.graphics.Wall;
import game.musics.BackgroundSound;
import game.musics.GameSoundtracks;
import game.physics.Circle;
import game.physics.Square;
import game.player.Player;
import game.player.PlayerStatus;
import game.views.SplashScreen;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;
import login.services.Game;
import shadow.math.SFVertex3f;

public class GameActivity extends Activity {

    public static final String LOG_TAG = "GameActivity";

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();
    private BackgroundSound backgroundSound;
    private GameManager gameManager;

    private CountDownLatch startSignal = new CountDownLatch(1);
    private GraphicsView graphicsView;

    private int groundWidth = 0;
    private int groundHeight = 0;
    private Map map;

    private String username;
    private int hashcode;

    private Player me;
    private ArrayList<Player> otherPlayers;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game);
        context = this;

        backgroundSound = new BackgroundSound(context, new GameSoundtracks(R.raw.soundtrack_01, R.raw.soundtrack_02).getSoundtracks(context));

        Intent intent = getIntent();
        username = intent.getStringExtra(FieldsNames.USERNAME);
        hashcode = intent.getIntExtra(FieldsNames.HASHCODE, 0);

        Log.d(LOG_TAG, "Starting SplashScreen..");
        SplashScreen splashScreen = new SplashScreen(this, R.id.game_splash_container, R.id.game_splash_image, R.id.game_splash_text, startSignal);
        splashScreen.animate();

        gameManager = GameManager.getInstance();
        gameManager.setRoom(new Room("TestRoom", 10, 2)); //DEBUG
        serverCommunicationThread.setHandler(new GameHandler());
        Log.d(LOG_TAG, "Asking Map..");
        try {
            serverCommunicationThread.send(createMapRequest());
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void continueAfterMapReceived() {
        Log.d(LOG_TAG, "continueAfterMapReceived");
        gameManager.setMap(map);

        SFVertex3f position = new SFVertex3f(5, 0.5f, -7);
        SFVertex3f direction = new SFVertex3f(-1, -0.25f, 0);

        me = new Player(new PlayerStatus(direction, new Circle(position, 0.75)), "Me");
        otherPlayers = gameManager.getRoom().getPlayers();
        Iterator<Player> iterator = otherPlayers.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getName().equals(username)) {
                me = p;
                iterator.remove();
            }
        }

        Log.d(LOG_TAG, "Starting GraphicsView..");
        final LinearLayout graphicsContainerLayout = (LinearLayout) findViewById(R.id.graphics_view_container);
        graphicsView = new GraphicsView(context, me, otherPlayers, gameManager.getMap(), startSignal, groundWidth, groundHeight);
        graphicsContainerLayout.addView(graphicsView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundSound.start();

        if (graphicsView != null)
            graphicsView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundSound.stop();

        if (graphicsView != null)
            graphicsView.onPause();
    }

    private JSONObject createMapRequest() {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.GAME);
            request.put(FieldsNames.SERVICE_TYPE, FieldsNames.GAME_MAP);
            request.put(FieldsNames.HASHCODE, hashcode);
            request.put(FieldsNames.USERNAME, username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    public class GameHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Log.d(LOG_TAG, "handleMessage");
            switch (msg.what) {
                case Game.STATUS:
                    break;
                case Game.MAP:
                    processMapMessage(msg);
                    break;
                case Game.POSITIONS:
                    break;
            }
        }

        private void processMapMessage(Message msg) {
            Log.d(LOG_TAG, "processMapMessage");
            Game.GameMapResult results = (Game.GameMapResult) msg.obj;
            map = new Map();

            for (Game.GameMapObject o : results.getGameMapObjects()) {
                if (o.object.equals("Wall")) {
                    float posX = Float.parseFloat(o.position.split(" ")[0]);
                    float posY = Float.parseFloat(o.position.split(" ")[1]);
                    float posZ = Float.parseFloat(o.position.split(" ")[2]);
                    float sizeX = Float.parseFloat(o.size.split(" ")[0]);
                    float sizeY = Float.parseFloat(o.size.split(" ")[1]);
                    float sizeZ = Float.parseFloat(o.size.split(" ")[2]);
                    int texture = TextureCodes.getTextureIdFromString(o.texture);
                    map.addObjects(new Wall(new Square(new SFVertex3f(posX, posY, posZ), sizeX, sizeY, sizeZ), texture));
                } else if (o.object.equals("Obstacle")) {
                    float posX = Float.parseFloat(o.position.split(" ")[0]);
                    float posY = Float.parseFloat(o.position.split(" ")[1]);
                    float posZ = Float.parseFloat(o.position.split(" ")[2]);
                    float sizeX = Float.parseFloat(o.size.split(" ")[0]);
                    float sizeY = Float.parseFloat(o.size.split(" ")[1]);
                    int texture = TextureCodes.getTextureIdFromString(o.texture);
                    map.addObjects(new Obstacle(new Circle(new SFVertex3f(posX, posY, posZ), sizeX), sizeY, texture));
                }
            }

            groundWidth = results.getWidth();
            groundHeight = results.getHeight();

            continueAfterMapReceived();
        }
    }

}
