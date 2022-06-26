package game.listeners;

/**
 * Listener for the events about positions during the movement.
 *
 * @author Torlaschi
 * @date 28/03/2015
 */
public interface PositionMoveListenerInterface {

    /**
     * Performs a position move action.
     *
     * @param angleXZ Angle over the XZ plane in which direction to move.
     * @param angleYZ Angle over the YZ plane in which direction to move.
     * @param delta   Timestamp that indicated how long to move.
     */
    void move(float angleXZ, float angleYZ, long delta);

}
