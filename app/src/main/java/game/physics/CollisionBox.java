package game.physics;

import java.util.ArrayList;

import graphic.shadow.math.SFVertex3f;

/**
 * This is an interface for generic 2D collision box, for a system based on SAT
 * (Separating Axis Theorem).
 *
 * @author De Pace
 */
public interface CollisionBox {

    /**
     * @return <code>SFVertex3f</code> which represent the <code>CollisionBox</code> center.
     */
    SFVertex3f getPosition();

    /**
     * @return The radio of the circle which is circumscribed to the geometrical form of the <code>CollisionBox</code>.
     */
    float getRadius();

    /**
     * @param otherCenter Other CollisionBox center compared to which collision is calculated.
     * @return List of normalized vectors, which are orthogonal to the sides.
     */
    ArrayList<SFVertex3f> getAxes(SFVertex3f otherCenter);

    /**
     * @param ax Normalized vector along whose dimension the projections has to be calculated.
     * @return Array whose component are the inferior extreme and the superior extreme of the
     * figure projection along the specified axes.
     */
    float[] getProjections(SFVertex3f ax);

}
