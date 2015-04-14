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
import game.physics.Circle;
import game.physics.Square;
import game.player.Player;
import game.player.PlayerStatus;
import shadow.math.SFVertex3f;

public class MainActivity extends Activity {

    private CountDownLatch startSignal = new CountDownLatch(1);
    private LinearLayout graphicsContainerLayout;
    private FrameLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        SFVertex3f position = new SFVertex3f(10, 0.5f, 4);
        SFVertex3f direction = new SFVertex3f(0, -0.25f, -1);

        Player me = new Player(new PlayerStatus(direction, new Circle(position, 1)), "Me");
        ArrayList<Player> otherPlayers = new ArrayList<>();

        Map map = new Map();
        map.addObjects("Wall.obj", R.drawable.wall_texture_02,
                new Square(new SFVertex3f(5, 0, -1), 2, 2, 2),
                new Square(new SFVertex3f(5, 0, -3), 2, 2, 2),
                new Square(new SFVertex3f(3, 0, -5), 2, 2, 2),
                new Square(new SFVertex3f(1, 0, -5), 2, 2, 2),
                new Square(new SFVertex3f(-1, 0, -5), 2, 2, 2),
                new Square(new SFVertex3f(-1, 0, -3), 2, 2, 2),
                new Square(new SFVertex3f(-1, 0, 0), 2, 2, 2),
                new Square(new SFVertex3f(-3, 0, 0), 2, 2, 2)
        );

        splashLayout = (FrameLayout) findViewById(R.id.splash_screen);
        graphicsContainerLayout = (LinearLayout) findViewById(R.id.graphics_view_container);

        startSplashScreenThread();

        graphicsContainerLayout.addView(new GraphicsView(this, me, otherPlayers, map, startSignal));
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
