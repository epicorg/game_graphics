package game.net.interpreters;

import android.os.Message;

import java.util.HashMap;
import java.util.Objects;

import game.data.Room;
import game.net.services.Game;
import game.player.Player;

/**
 * Responsible for interpreting position data and updating all the other {@link Player}'s {@link game.player.PlayerStatus}.
 *
 * @see Room
 */
public class PositionsInterpreter implements Interpreter {

    private final Room room;

    /**
     * Creates a new <code>PositionInterpreter</code>.
     *
     * @param room <code>Room</code> which contains players to which update position data.
     */
    public PositionsInterpreter(Room room) {
        this.room = room;
    }

    @Override
    public int getKey() {
        return Game.POSITIONS;
    }

    @Override
    public void interpret(Message message) {
        Game.GamePositionsResult results = (Game.GamePositionsResult) message.obj;
        HashMap<String, Game.GamePositionsObject> gamePositionsObjectHashMap = results.getGamePositionsObjectHashMap();

        for (String s : gamePositionsObjectHashMap.keySet()) {
            Player p = room.getPlayerByUsername(s);
            p.getStatus().getPosition().set(Objects.requireNonNull(gamePositionsObjectHashMap.get(s)).pos);
            p.getStatus().getDirection().set(Objects.requireNonNull(gamePositionsObjectHashMap.get(s)).dir);
        }
    }

}
