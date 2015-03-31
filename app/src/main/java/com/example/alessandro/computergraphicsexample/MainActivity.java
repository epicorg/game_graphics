package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

import game.physics.Box;
import game.player.Player;
import game.player.PlayerStatus;
import shadow.math.SFVertex3f;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SFVertex3f position = new SFVertex3f();
        position.set3f(0, 0.5f, 2);
        SFVertex3f direction = new SFVertex3f();
        direction.set3f(0, -0.25f, -1);

        Player me = new Player(new PlayerStatus(position, direction, new Box(2, 2, 2)), "Me");
        ArrayList<Player> otherPlayers = new ArrayList<Player>();

        setContentView(new GraphicsView(this, me, otherPlayers));
    }

}
