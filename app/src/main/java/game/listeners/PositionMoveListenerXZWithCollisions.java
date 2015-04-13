package game.listeners;

import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import game.physics.CollisionUtils;
import game.player.PlayerStatus;
import shadow.math.SFMatrix3f;
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
        SFVertex3f position=playerStatus.getCollisionBox().getPos(), originalPosition = new SFVertex3f(position);

        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tempVertex = rotationMatrix.Mult(new SFVertex3f(playerStatus.getDirection().getX(), 0, playerStatus.getDirection().getZ()));
        tempVertex.normalize3f();
        tempVertex.mult(MOVE_SPEED * delta / 1000);

        position.add3f(tempVertex);

        CollisionBox box=cm.collide(playerStatus.getCollisionBox());
        // Correct for obstacles
        if (box!=null)
            correctMotion(position, originalPosition, tempVertex, box);
        box=cm.collide(playerStatus.getCollisionBox());
        // Correct for junctions
        if (box!=null)
            correctMotion(position, originalPosition, tempVertex, box);
        // Reset if blocked
        if (cm.collide(playerStatus.getCollisionBox())!=null)
            position.set(originalPosition);
        else
            playerStatus.getPosition().set(position);
    }

    public void correctMotion(SFVertex3f v, SFVertex3f v0, SFVertex3f tempVertex, CollisionBox box){
        SFVertex3f vertex;
        for (int i = 0; i < NUMBER_OF_ANGLE_DIVISIONS; i++) {
            for (int j = -1; j < 2; j+=2) {
                SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(i*j*(float)Math.PI/(2*NUMBER_OF_ANGLE_DIVISIONS));
                vertex=new SFVertex3f(tempVertex);
                vertex = rotationMatrix.Mult(vertex);
                v.set(v0);
                v.add(vertex);
                if (!CollisionUtils.checkCollision(playerStatus.getCollisionBox(), box)){
                    return;
                }
            }
        }
    }

}
