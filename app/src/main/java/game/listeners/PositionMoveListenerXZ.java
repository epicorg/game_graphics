package game.listeners;

import android.util.Log;

import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class PositionMoveListenerXZ implements PositionMoveListenerInterface {

    public static final String LOG_TAG = "PositionMoveListenerXZ";

    private SFVertex3f position;
    private SFVertex3f direction;

    public PositionMoveListenerXZ(SFVertex3f position, SFVertex3f direction) {
        this.position = position;
        this.direction = direction;
    }

    @Override
    public void move(float angleXZ, float angleYZ) {
        //Ignore angleYZ for now;

        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(angleXZ);
        SFVertex3f tmp1 = new SFVertex3f();
        tmp1.set3f(direction.getX(), 0, direction.getZ());
        SFVertex3f tmp2 = rotationMatrix.Mult(tmp1);
        tmp2.normalize3f();
        tmp2.mult(0.5f);

        float nextX = position.getX() + tmp2.getX();
        float nextY = position.getY();
        float nextZ = position.getZ() + tmp2.getZ();
        position.set3f(nextX, nextY, nextZ);
    }
}
