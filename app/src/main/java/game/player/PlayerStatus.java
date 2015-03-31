package game.player;

import android.util.Log;
import game.physics.Collidable;
import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import game.physics.Vector3D;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 28/03/2015.
 */
public class PlayerStatus implements Collidable{

    private SFVertex3f position;
    private SFVertex3f direction;
    private CollisionBox box;

    public PlayerStatus(SFVertex3f position, SFVertex3f direction, CollisionBox box) {
        this.position = position;
        this.direction = direction;
        this.box=box;
    }

    public SFVertex3f getPosition() {
        return position;
    }

    public SFVertex3f getDirection() {
        return direction;
    }

    public CollisionBox getBox(){
        box.setPos(position);
        return box;
    }

}
