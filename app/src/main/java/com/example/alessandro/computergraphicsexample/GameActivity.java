package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import game.graphics.Map;
import game.graphics.Obstacle;
import game.graphics.Wall;
import game.physics.Circle;
import game.physics.Square;
import game.player.Player;
import game.player.PlayerStatus;
import shadow.math.SFVertex3f;

public class GameActivity extends Activity {

    private CountDownLatch startSignal = new CountDownLatch(1);
    private LinearLayout graphicsContainerLayout;
    private FrameLayout splashLayout;
    private GraphicsView graphicsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        SFVertex3f position = new SFVertex3f(5, 0.5f, -7);
        SFVertex3f direction = new SFVertex3f(-1, -0.25f, 0);

        Player me = new Player(new PlayerStatus(direction, new Circle(position, 0.75)), "Me");
        ArrayList<Player> otherPlayers = new ArrayList<>();

        Map map = new Map();
        double h = 2.5;
        int texture_id=R.drawable.wall_texture_03;
        map.addObjects(new Wall(new Square(new SFVertex3f(-9.25, -1, -2.75), 1.5, h, 14.5), texture_id),
                new Wall(new Square(new SFVertex3f(-6.75, -1, -4.25), 3.5, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(-1, -1, -9.25), 15, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(7.25, -1, -2.75), 1.5, h, 14.5), texture_id),
                new Wall(new Square(new SFVertex3f(4, -1, -4.25), 5, h, 1.5), texture_id),
                new Obstacle(new Circle(new SFVertex3f(-0.7, -1, -4.25),0.3),h,R.drawable.obstacle_texture_01),
                new Obstacle(new Circle(new SFVertex3f(-2.9, -1, -4.25),0.3),h,R.drawable.obstacle_texture_01),
                new Wall(new Square(new SFVertex3f(3.75, -1, 3.75), 5.5, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(-4.75, -1, 3.75), 7.5, h, 1.5), texture_id),
                new Wall(new Square(new SFVertex3f(-1, -1, -0.25), 10, h, 1.5), texture_id)
        );

        splashLayout = (FrameLayout) findViewById(R.id.splash_screen);
        graphicsContainerLayout = (LinearLayout) findViewById(R.id.graphics_view_container);

        startSplashScreenThread();

        graphicsView = new GraphicsView(this, me, otherPlayers, map, startSignal);
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

    private void startSplashScreenThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startSignal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        splashLayout.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }

}
