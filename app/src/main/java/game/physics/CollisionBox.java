package game.physics;

import java.util.ArrayList;
import shadow.math.SFVertex3f;

/**
 * Created by depa on 06/04/15.
 */
public interface CollisionBox {

    public SFVertex3f getPos();
    public float getRadius();
    public ArrayList<SFVertex3f> getAxes(SFVertex3f otherCenter);
    public float[] getProjections(SFVertex3f ax);

}
