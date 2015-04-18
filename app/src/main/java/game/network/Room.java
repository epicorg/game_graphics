package game.network;

import java.util.ArrayList;

import game.player.Player;

/**
 * Created by Andrea on 18/04/2015.
 */
public class Room {

    private String name;
    private int maxPlayers, currentPlayers;

    public Room(String name, int maxPlayers, int currentPlayers) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
    }

    public void join() {
        //TODO
    }

    public ArrayList<Player> getPlayers() {
        //TODO

        ArrayList<Player> players = new ArrayList<>();
        return players;
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    @Override
    public String toString() {
        return getName() + " (" + getCurrentPlayers() + " / " + getMaxPlayers() + ")";
    }

}
