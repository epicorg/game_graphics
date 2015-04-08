package game.listeners;

import game.physics.CollisionMediator;
import game.player.PlayerStatus;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class PositionMoveListenerXZWithCollisions implements PositionMoveListenerInterface {

    public static final String LOG_TAG = "PositionMoveListenerXZ";

    private static final int NUMBER_OF_ANGLE_DIVISIONS = 50;
    private static float MOVE_SPEED = 3.00f; // Distance/s

    private PlayerStatus playerStatus;
    private CollisionMediator cm;

    public PositionMoveListenerXZWithCollisions(PlayerStatus playerStatus, CollisionMediator cm) {
        this.playerStatus = playerStatus;
        this.cm = cm;
    }

    @Override
    public void move(float angleXZ, float angleYZ, long delta) {
        SFVertex3f originalPosition = new SFVertex3f(playerStatus.getPosition());

        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tempVertex = rotationMatrix.Mult(new SFVertex3f(playerStatus.getDirection().getX(), 0, playerStatus.getDirection().getZ()));
        tempVertex.normalize3f();
        tempVertex.mult(MOVE_SPEED * delta / 1000);
        playerStatus.getPosition().add3f(tempVertex);

        int i = 0;
        while (cm.collide(playerStatus.getCollisionBox())) {
            playerStatus.getPosition().set(originalPosition);
            playerStatus.getPosition().add3f(correctMotion(tempVertex, i, NUMBER_OF_ANGLE_DIVISIONS));

            i++;
            if (i == NUMBER_OF_ANGLE_DIVISIONS * 2) {
                playerStatus.getPosition().set(originalPosition);
                break;
            }
        }
    }

    private SFVertex3f correctMotion(SFVertex3f motion, int i, int n) {
        int sign = (i % 2) == 0 ? +1 : -1;

        SFVertex3f kv = new SFVertex3f(motion);
        SFTransform3f rot = new SFTransform3f();
        rot.setMatrix(SFMatrix3f.getRotationY((float) (sign * Math.PI * 0.5 * (i / 2) / n)));
        rot.transform(kv);

        SFVertex3f motion2 = new SFVertex3f(motion);
        kv.mult3f(motion2.dot3f(kv) / (kv.getSquareModulus()));
        return kv;
    }

}
