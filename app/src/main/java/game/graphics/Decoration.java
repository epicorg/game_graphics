package game.graphics;

import android.content.Context;

import game.generators.FundamentalGenerator;
import game.physics.CollisionBox;
import graphic.integration.Node;
import graphic.shadow.math.SFMatrix3f;
import graphic.shadow.math.SFVertex3f;

/**
 * Decoration of Object. Basically a collision-free <code>MazeObject</code> with customizable 3D model.
 *
 * @author De Pace
 * @see graphic.integration.Model
 */
public class Decoration implements MazeObject {

    private final int textureId;
    private final String model;
    private final SFVertex3f position;
    private final float scaleXZ, scaleY;
    private final boolean supportCulling;

    /**
     * Creates a new <code>Decoration</code>.
     *
     * @param position       position of the <code>Decoration</code>
     * @param model          string name of the 3D model file to represent this <code>Decoration</code>
     * @param textureId      ID of the texture that represents this <code>Decoration</code>
     * @param scaleXZ        scale factor on the x-z plane
     * @param scaleY         scale factor in the y direction
     * @param supportCulling if this object support face culling
     */
    public Decoration(SFVertex3f position, String model, int textureId, float scaleXZ, float scaleY, boolean supportCulling) {
        this.textureId = textureId;
        this.model = model;
        this.position = position;
        this.scaleXZ = scaleXZ;
        this.scaleY = scaleY;
        this.supportCulling = supportCulling;
    }

    @Override
    public Node getNode(Context context) {
        Node node = new Node();
        node.setModel(FundamentalGenerator.getModel(context,
                ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER), textureId, model));
        node.getRelativeTransform().setPosition(position);
        node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(scaleXZ, scaleY, scaleXZ));
        return node;
    }

    @Override
    public CollisionBox getBox() {
        return null;
    }

    @Override
    public boolean supportingCulling() {
        return supportCulling;
    }

    @Override
    public MazeObject cloneFromData(String position, String size, int textureId) {
        float posX = Float.parseFloat(position.split(" ")[0]);
        float posY = Float.parseFloat(position.split(" ")[1]);
        float posZ = Float.parseFloat(position.split(" ")[2]);
        float sizeX = Float.parseFloat(size.split(" ")[0]);
        float sizeY = Float.parseFloat(size.split(" ")[1]);
        return new Decoration(new SFVertex3f(posX, posY, posZ), model, textureId, sizeX, sizeY, supportCulling);
    }

}

