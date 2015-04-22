package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import game.GameManager;
import game.Room;
import game.graphics.Map;
import game.graphics.Obstacle;
import game.graphics.Wall;
import game.physics.Circle;
import game.physics.Square;
import game.player.Player;
import game.player.PlayerStatus;
import game.views.SplashScreen;
import shadow.math.SFVertex3f;

public class GameActivity extends Activity {

    public static final String LOG_TAG = "GameActivity";

    private GameManager gameManager;

    private CountDownLatch startSignal = new CountDownLatch(1);
    private GraphicsView graphicsView;

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

        gameManager = GameManager.getInstance();

        SFVertex3f position = new SFVertex3f(5, 0.5f, -7);
        SFVertex3f direction = new SFVertex3f(-1, -0.25f, 0);

        me = new Player(new PlayerStatus(direction, new Circle(position, 0.75)), "Me");

        //DEBUG
        gameManager.setRoom(new Room("TestRoom", 10, 2));

        otherPlayers = gameManager.getRoom().getPlayers();
        Iterator<Player> iterator = otherPlayers.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getName().equals(username)) {
                me = p;
                iterator.remove();
            }
        }

        final Map map = new Map();
        final double h = 2.5;
        int texture_id = R.drawable.wall_texture_03;
        map.addObjects(new Wall(new Square(new SFVertex3f(-9.25, -1, -2.75), 1.5, h, 14.5), texture_id),
                new Wall(new Square(new SFVertex3f(-6.75, -1, -4.25), 3.5, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(-1, -1, -9.25), 15, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(7.25, -1, -2.75), 1.5, h, 14.5), texture_id),
                new Wall(new Square(new SFVertex3f(4, -1, -4.25), 5, h, 1.5), texture_id),
                new Obstacle(new Circle(new SFVertex3f(-0.7, -1, -4.25), 0.3), h, R.drawable.obstacle_texture_01),
                new Obstacle(new Circle(new SFVertex3f(-2.9, -1, -4.25), 0.3), h, R.drawable.obstacle_texture_01),
                new Wall(new Square(new SFVertex3f(3.75, -1, 3.75), 5.5, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(-4.75, -1, 3.75), 7.5, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(-1, -1, -0.25), 10, h, 1.5), texture_id)
        );

        final int groundDim = 20;
        final double h2 = 2;
        map.addObjects(new Wall(new Square(new SFVertex3f(0, -1, groundDim), 2 * groundDim, h2, 2), R.drawable.hedge_texture_02_1));
        map.addObjects(new Wall(new Square(new SFVertex3f(0, -1, -groundDim), 2 * groundDim, h2, 2), R.drawable.hedge_texture_02_1));
        map.addObjects(new Wall(new Square(new SFVertex3f(groundDim, -1, 0), 2, h2, 2 * groundDim - 2), R.drawable.hedge_texture_02_1));
        map.addObjects(new Wall(new Square(new SFVertex3f(-groundDim, -1, 0), 2, h2, 2 * groundDim - 2), R.drawable.hedge_texture_02_1));
        gameManager.setMap(map);

        final LinearLayout graphicsContainerLayout = (LinearLayout) findViewById(R.id.graphics_view_container);

        Log.d(LOG_TAG, "Starting SplashScreen..");
        SplashScreen splashScreen = new SplashScreen(this, R.id.game_splash_container, R.id.game_splash_image, R.id.game_splash_text, startSignal);
        splashScreen.animate();

        Log.d(LOG_TAG, "Starting GraphicsView..");
        graphicsView = new GraphicsView(context, me, otherPlayers, gameManager.getMap(), startSignal, groundDim);
        graphicsContainerLayout.addView(graphicsView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        graphicsView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        graphicsView.onPause();
    }

}
