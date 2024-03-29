package game.graphics;

import android.content.Context;

import game.physics.CollisionBox;
import graphic.integration.Node;

/**
 * It represents a generic object in the labyrinth.
 *
 * @author De Pace
 * @see CollisionBox
 * @see Node
 */
public interface MazeObject {

    /**
     * Returns a <code>Node</code> representing the <code>MazeObject</code>.
     *
     * @param context <code>Context</code> to find the resources to represent the object
     * @return <code>Node</code> representing the object
     */
    Node getNode(Context context);

    /**
     * Returns the <code>MazeObject</code>'s <code>CollisionBox</code> for collision checking.
     *
     * @return the object <code>CollisionBox</code>; 'null' if the Object doesn't have to be in the collision system
     */
    CollisionBox getBox();

    /**
     * Returns if this <code>MazeObject</code> can be drawn with culling enabled.
     *
     * @return true if culling is supported, false otherwise
     */
    boolean supportingCulling();

    /**
     * Allows to create different <code>MazeObject</code> from a single prototype, with different parameters.
     *
     * @param position  <code>CollisionBox</code> position
     * @param size      <code>CollisionBox</code> dimension
     * @param textureId <code>CollisionBox</code> texture ID
     * @return <code>CollisionBox</code> which is built from the specified parameters
     */
    MazeObject cloneFromData(String position, String size, int textureId);

}
