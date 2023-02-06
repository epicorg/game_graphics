package game.listeners;

import graphic.shadow.math.SFMatrix3f;
import graphic.shadow.math.SFTransform3f;
import graphic.shadow.math.SFVertex3f;

/**
 * Listener for motion of a {@link SFVertex3f} direction.
 */
public class DirectionDirectionMoveListener implements DirectionMoveListenerInterface {

    private static final String LOG_TAG = "DirectionDirectionMove";

    private final SFVertex3f direction;
    private float moveFactorX;
    private float moveFactorY;

    private final float oldXZLength;

    /**
     * Creates a new <code>DirectionDirectionMoveListener</code>.
     *
     * @param direction     <code>SFVertex3f</code> direction to modify
     * @param displayWidth  actual display width
     * @param displayHeight actual display height
     */
    public DirectionDirectionMoveListener(SFVertex3f direction, float displayWidth, float displayHeight) {
        this.direction = direction;
        this.moveFactorX = 1 / displayWidth;
        this.moveFactorY = 1 / displayHeight;
        oldXZLength = (float) Math.sqrt(direction.getX() * direction.getX() + direction.getZ() * direction.getZ());
    }

    @Override
    public void update(int width, int height) {
        this.moveFactorX = 1 / (float) width;
        this.moveFactorY = 1 / (float) height;
    }

    @Override
    public void move(float dx, float dy) {
        float oldLength = direction.getLength();
        float oldVerAngle = (float) Math.atan2(direction.getY(), oldXZLength);
        float newVerAngle = oldVerAngle + (float) (dy * moveFactorY * Math.PI);
        float newYLength = (float) Math.tan(newVerAngle) * oldXZLength;

        SFVertex3f newVertex = new SFVertex3f();
        newVertex.set3f(direction.getX(), newYLength, direction.getZ());
        newVertex.normalize3f();
        newVertex.mult3f(oldLength);
        direction.set3f(newVertex.getX(), newVertex.getY(), newVertex.getZ());

        SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY((float) (dx * moveFactorX * Math.PI));
        SFTransform3f transform3f = new SFTransform3f();
        transform3f.setMatrix(rotationMatrix);
        transform3f.transform(direction);
    }

}
