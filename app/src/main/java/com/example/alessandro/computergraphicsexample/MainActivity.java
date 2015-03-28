package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;

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

        Player me = new Player(new PlayerStatus(position, direction), "Me");
        ArrayList<Player> otherPlayers = new ArrayList<Player>();

        setContentView(new GraphicsView(this, getWindowManager(), me, otherPlayers));
    }

}
