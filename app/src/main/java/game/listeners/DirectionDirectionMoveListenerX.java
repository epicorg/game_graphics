package game.listeners;

import android.util.Log;

import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class DirectionDirectionMoveListenerX implements DirectionMoveListenerInterface {

    public static final String LOG_TAG = "DirectionDirectionMove";

    private SFVertex3f direction;
    private float moveFactorX;
    private float moveFactorY;

    public DirectionDirectionMoveListenerX(SFVertex3f direction, float displayWidth, float displayHeight) {
        this.direction = direction;
        this.moveFactorX = 1 / displayWidth;
        this.moveFactorY = 1 / displayHeight;
    }

    @Override
    public void move(float dx, float dy) {
        float oldLenght = direction.getLength();
        float oldXZLenght = (float) Math.sqrt(direction.getX() * direction.getX() + direction.getZ() * direction.getZ());
        float oldVerAngle = (float) Math.atan2(direction.getY(), oldXZLenght);

        float newVerAngle = oldVerAngle + (float) (dy * moveFactorY * Math.PI);
        float newYLenght = (float) Math.tan(newVerAngle) * oldXZLenght;
        SFVertex3f newVertex = new SFVertex3f();
        newVertex.set3f(direction.getX(), newYLenght, direction.getZ());
        newVertex.normalize3f();
        newVertex.mult3f(oldLenght);

        direction.set3f(newVertex.getX(), newVertex.getY(), newVertex.getZ());

        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY((float) (dx * moveFactorX * Math.PI));
        SFTransform3f trfy = new SFTransform3f();
        trfy.setMatrix(rotationMatrix);
        trfy.transform(direction);
    }
}
