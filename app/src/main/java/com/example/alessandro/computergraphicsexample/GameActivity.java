package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import game.GameManager;
import game.graphics.Map;
import game.graphics.Obstacle;
import game.graphics.Wall;
import game.physics.Circle;
import game.physics.Square;
import game.player.Player;
import game.player.PlayerStatus;
import shadow.math.SFVertex3f;

public class GameActivity extends Activity {

    private GameManager gameManager;

    private CountDownLatch startSignal = new CountDownLatch(1);
    private LinearLayout graphicsContainerLayout;
    private FrameLayout splashLayout;
    private GraphicsView graphicsView;
    private int groundDim = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game);

        animateImage();
        animateText();

        gameManager = GameManager.getInstance();

        SFVertex3f position = new SFVertex3f(5, 0.5f, -7);
        SFVertex3f direction = new SFVertex3f(-1, -0.25f, 0);

        Player me = new Player(new PlayerStatus(direction, new Circle(position, 0.75)), "Me");
        ArrayList<Player> otherPlayers = new ArrayList<>();

        Map map = new Map();
        double h = 2.5;
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

        double h2 = 2;
        map.addObjects(new Wall(new Square(new SFVertex3f(0, -1, groundDim), 2 * groundDim, h2, 2), R.drawable.hedge_texture_02_1));
        map.addObjects(new Wall(new Square(new SFVertex3f(0, -1, -groundDim), 2 * groundDim, h2, 2), R.drawable.hedge_texture_02_1));
        map.addObjects(new Wall(new Square(new SFVertex3f(groundDim, -1, 0), 2, h2, 2 * groundDim - 2), R.drawable.hedge_texture_02_1));
        map.addObjects(new Wall(new Square(new SFVertex3f(-groundDim, -1, 0), 2, h2, 2 * groundDim - 2), R.drawable.hedge_texture_02_1));
        gameManager.setMap(map);

        splashLayout = (FrameLayout) findViewById(R.id.splash_screen);
        graphicsContainerLayout = (LinearLayout) findViewById(R.id.graphics_view_container);

        startSplashScreenThread();

        graphicsView = new GraphicsView(this, me, otherPlayers, gameManager.getMap(), startSignal, groundDim);
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

    private void animateImage() {
        final ImageView myImage = (ImageView) findViewById(R.id.imageSplash);

        final Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotator);

        final android.view.animation.Interpolator li = new LinearInterpolator();

        myImage.startAnimation(myRotation);

        myRotation.setInterpolator(li);

        myRotation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotator);
                myRotation.setAnimationListener(this);
                myRotation.setInterpolator(li);
                myImage.startAnimation(myRotation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void animateText() {
        final TextView textView = (TextView) findViewById(R.id.loadText);
        final Animation fadeIn, fadeOut;

        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        textView.startAnimation(fadeIn);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.startAnimation(fadeIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
