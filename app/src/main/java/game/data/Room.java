package game.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import game.player.Player;

/**
 * This class represents the room in which the users enter. Here the teams are created.
 *
 * @author Torlaschi
 * @date 18/04/2015
 * @see Team
 * @see Player
 */
public class Room {

    private final String name;
    private final int maxPlayers;
    private int currentPlayers;
    private ArrayList<Team> teams;

    /**
     * Creates a <code>Room</code> with given name and number of <code>Player</code>.
     *
     * @param name           name of the <code>Room</code>
     * @param maxPlayers     max number of <code>Player</code> in this <code>Room</code>
     * @param currentPlayers number of current <code>Player</code> in this <code>Room</code>
     */
    public Room(String name, int maxPlayers, int currentPlayers) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
    }

    /**
     * Creates a <code>Room</code> with given name and number of players.
     *
     * @param name       name of the <code>Room</code>
     * @param maxPlayers max number of players in this <code>Room</code>
     * @param teams      current list of <code>Player</code> in this <code>Room</code>
     */
    public Room(String name, int maxPlayers, ArrayList<Team> teams) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.teams = teams;

        currentPlayers = 0;
        for (Team t : teams)
            for (Player ignored : t.getPlayers())
                currentPlayers++;
    }

    /**
     * @return the <code>Room</code> name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the max number of <code>Player</code> in this <code>Room</code>
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return the current number of <code>Player</code>
     */
    public int getCurrentPlayers() {
        return currentPlayers;
    }

    /**
     * @return the <code>Team</code> list of this the requested <code>Room</code>
     */
    public ArrayList<Team> getTeams() {
        return teams;
    }

    /**
     * Returns a <code>Player</code> in this <code>Room</code> given the username.
     *
     * @param username the username of the requested <code>Player</code>
     * @return the requested <code>Player</code>
     */
    public Player getPlayerByUsername(String username) {
        for (Team t : teams)
            for (Player p : t.getPlayers())
                if (p.getName().equals(username))
                    return p;
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " (" + getCurrentPlayers() + " / " + getMaxPlayers() + ")";
    }

}
