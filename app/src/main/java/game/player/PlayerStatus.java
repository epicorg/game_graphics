package game.player;

import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class PlayerStatus {

    private SFVertex3f position;
    private SFVertex3f direction;

    public PlayerStatus(SFVertex3f position, SFVertex3f direction) {
        this.position = position;
        this.direction = direction;
    }

    public SFVertex3f getPosition() {
        return position;
    }

    public SFVertex3f getDirection() {
        return direction;
    }

}
