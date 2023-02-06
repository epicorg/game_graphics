package game.player;

import androidx.annotation.NonNull;

import game.physics.Circle;
import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import graphic.shadow.math.SFMatrix3f;
import graphic.shadow.math.SFVertex3f;

/**
 * This class manages the status of a {@link Player}.
 *
 * @author Torlaschi
 * @date 28/03/2015
 * @see Player
 * @see Circle
 */
public class PlayerStatus {

    private static final int NUMBER_OF_ANGLE_DIVISIONS = 50;
    private final SFVertex3f position;
    private final SFVertex3f direction;
    private final Circle circle;

    /**
     * Creates a new <code>PlayerStatus</code> with a given direction and <code>Circle</code> as <code>CollisionBox</code>.
     *
     * @param direction direction towards which the <code>Player</code> is looking
     * @param circle    circle that represents the <code>Player</code>'s <code>CollisionBox</code>
     */
    public PlayerStatus(SFVertex3f direction, Circle circle) {
        this.position = new SFVertex3f(circle.getPosition());
        this.direction = direction;
        this.circle = circle;
    }

    /**
     * Sets <code>PlayerStatus</code> position.
     *
     * @param x x-component of the position
     * @param y y-component of the position
     * @param z z-component of the position
     */
    public void setPositionValue(float x, float y, float z) {
        position.set3f(x, y, z);
        circle.getPosition().set3f(x, y, z);
    }

    /**
     * Moves the <code>PlayerStatus</code> for a distance and direction given.
     *
     * @param motion represents direction and distance of motion
     * @param cm     manages the collisions with other <code>CollisionBox</code>
     */
    public void move(SFVertex3f motion, CollisionMediator cm) {
        circle.getPosition().set(position);
        SFVertex3f circlePosition = circle.getPosition();
        SFVertex3f originalPosition = new SFVertex3f(circlePosition);
        circlePosition.add3f(motion);

        CollisionBox collisionBox = cm.collide(circle);

        // Correct for obstacles
        if (collisionBox != null)
            correctMotion(circlePosition, originalPosition, motion, collisionBox);

        collisionBox = cm.collide(circle);

        // Correct for junctions
        if (collisionBox != null)
            correctMotion(circlePosition, originalPosition, motion, collisionBox);

        // Reset if blocked, else update effective position
        CollisionBox box2 = cm.collide(circle);
        if (box2 != null)
            circlePosition.set(originalPosition);
        else
            position.set(circlePosition);
    }

    private void correctMotion(SFVertex3f v, SFVertex3f v0, SFVertex3f tempVertex, CollisionBox box) {
        SFVertex3f vertex;
        for (int i = 0; i < NUMBER_OF_ANGLE_DIVISIONS; i++) {
            for (int j = -1; j < 2; j += 2) {
                SFMatrix3f rotationMatrix = SFMatrix3f.getRotationY(i * j * (float) Math.PI / (2 * NUMBER_OF_ANGLE_DIVISIONS));
                vertex = new SFVertex3f(tempVertex);
                vertex = rotationMatrix.mult(vertex);
                v.set(v0);
                v.add(vertex);
                if (!CollisionMediator.checkCollision(circle, box)) {
                    return;
                }
            }
        }
    }

    public SFVertex3f getPosition() {
        return position;
    }

    public SFVertex3f getDirection() {
        return direction;
    }

    @NonNull
    @Override
    public String toString() {
        return "POS: " + getPosition().getX() + " - " + getPosition().getY() + " - " + getPosition().getZ() +
                "DIR: " + getDirection().getX() + " - " + getDirection().getY() + " - " + getDirection().getZ();
    }

}
