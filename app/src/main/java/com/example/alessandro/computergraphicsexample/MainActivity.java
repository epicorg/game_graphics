package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.ArrayList;

import game.graphics.Map;
import game.physics.Circle;
import game.physics.Square;
import game.player.Player;
import game.player.PlayerStatus;
import shadow.math.SFVertex3f;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        setContentView(new GraphicsView(this, me, otherPlayers, map));
    }

}
