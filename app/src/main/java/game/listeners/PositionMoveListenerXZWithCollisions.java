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

    private PlayerStatus playerStatus;
    private CollisionMediator cm;

    public PositionMoveListenerXZWithCollisions(PlayerStatus playerStatus, CollisionMediator cm) {
        this.playerStatus = playerStatus;
        this.cm = cm;
    }

    @Override
    public void move(float angleXZ, float angleYZ) {
        SFVertex3f originalPosition = new SFVertex3f(playerStatus.getPosition());

        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tmp1 = new SFVertex3f(playerStatus.getDirection().getX(), 0, playerStatus.getDirection().getZ());
        SFVertex3f tmp2 = rotationMatrix.Mult(tmp1);
        tmp2.normalize3f();
        tmp2.mult(0.5f);

        playerStatus.getPosition().add3f(tmp2);

        int i = 0;
        int numberOfAngleDivisions = 50;
        while (cm.collide(playerStatus.getCollisionBox())) {
            playerStatus.getPosition().set(originalPosition);
            SFVertex3f corrected = correctMotion(tmp2, i, numberOfAngleDivisions);

            playerStatus.getPosition().add3f(corrected);
            i++;

            if (i == numberOfAngleDivisions * 2) {
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
