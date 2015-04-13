package game.player;

import game.physics.Circle;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class PlayerStatus {

    private SFVertex3f position;
    private SFVertex3f direction;
    private Circle circle;

    public PlayerStatus(SFVertex3f direction, Circle circle) {
        this.position = new SFVertex3f(circle.getPos());
        this.direction = direction;
        this.circle = circle;
    }

    public SFVertex3f getPosition() {
        return position;
    }

    public SFVertex3f getDirection() {
        return direction;
    }

    public Circle getCollisionBox() {
        return circle;
    }

}
