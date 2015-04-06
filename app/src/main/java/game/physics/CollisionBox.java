package game.physics;

import shadow.math.SFVertex3f;

/**
 * Created by depa on 06/04/15.
 */
public interface CollisionBox {

    public void add(CollisionMediator cm);
    public SFVertex3f getPos();

}
