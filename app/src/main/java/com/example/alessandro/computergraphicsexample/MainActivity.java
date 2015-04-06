package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.os.Bundle;

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

        SFVertex3f position = new SFVertex3f(10, 0.5f, 4);
        SFVertex3f direction = new SFVertex3f(0, -0.25f, -1);

        Player me = new Player(new PlayerStatus(position, direction, new Circle(position, 1)), "Me");
        ArrayList<Player> otherPlayers = new ArrayList<Player>();

        Map map = new Map(this);
        map.addObjects("Wall.obj", R.drawable.wall_texture_02,
                new Square(new SFVertex3f(5, -1, -1), 2, 2, 2),
                new Square(new SFVertex3f(5, -1, -3), 2, 2, 2),
                new Square(new SFVertex3f(3, -1, -5), 2, 2, 2),
                new Square(new SFVertex3f(1, -1, -5), 2, 2, 2),
                new Square(new SFVertex3f(-1, -1, -5), 2, 2, 2),
                new Square(new SFVertex3f(-1, -1, -3), 2, 2, 2),
                new Square(new SFVertex3f(-1, -1, 0), 2, 2, 2),
                new Square(new SFVertex3f(-3, -1, 0), 2, 2, 2)
        );

        setContentView(new GraphicsView(this, me, otherPlayers, map));
    }

}
