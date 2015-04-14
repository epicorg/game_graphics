package game.graphics;

import android.content.Context;
import game.physics.CollisionBox;
import sfogl.integration.Node;

/**
 * Created by depa on 14/04/15.
 */
public interface MazeObject {

    public Node getNode(Context context);
    public CollisionBox getBox();

}
