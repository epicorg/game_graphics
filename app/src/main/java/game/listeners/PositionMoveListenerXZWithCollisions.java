package game.listeners;

import game.physics.CollisionMediator;
import game.player.PlayerStatus;
import graphic.shadow.math.SFMatrix3f;
import graphic.shadow.math.SFVertex3f;

/**
 * Performs action of movement taking collisions into account.
 *
 * @author Torlaschi
 * @see PlayerStatus
 * @see CollisionMediator
 */
public class PositionMoveListenerXZWithCollisions implements PositionMoveListenerInterface {

    private final float moveSpeed;
    private final PlayerStatus playerStatus;
    private final CollisionMediator collisionMediator;

    /**
     * Creates a new <code>PositionMoveListenerXZWithCollisions</code>.
     *
     * @param playerStatus      <code>PlayerStatus</code> to move.
     * @param collisionMediator <code>CollisionMediator</code> to perform collision checking.
     * @param moveSpeed         speed of motion.
     */
    public PositionMoveListenerXZWithCollisions(PlayerStatus playerStatus, CollisionMediator collisionMediator, float moveSpeed) {
        this.playerStatus = playerStatus;
        this.collisionMediator = collisionMediator;
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void move(float angleXZ, float angleYZ, long delta) {
        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tempVertex = rotationMatrix.mult(new SFVertex3f(playerStatus.getDirection().getX(), 0, playerStatus.getDirection().getZ()));
        tempVertex.normalize3f();
        tempVertex.multiply(moveSpeed * delta / 1000);
        playerStatus.move(tempVertex, collisionMediator);
    }

}
