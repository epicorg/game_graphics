package game.graphics;

import android.content.Context;

import game.generators.FundamentalGenerator;
import game.physics.Circle;
import game.physics.CollisionBox;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFVertex3f;

/**
 * Cylindrical obstacle.
 *
 * @author De Pace
 * @see Circle
 */
public class Obstacle implements MazeObject {

    private Circle c;
    private int texture_id;
    private float height;
    private String model;

    /**
     * Creates a new Obstacle from specified dimension and texture.
     *
     * @param c          <code>Circle</code> which makes up the <code>CollisionBox</code> and defines the dimension of xz plane.
     * @param height     Height in y direction.
     * @param texture_id Texture index.
     */
    public Obstacle(Circle c, double height, int texture_id, String model) {
        this.c = c;
        this.texture_id = texture_id;
        this.height = (float) height / 2;
        this.model = model;
    }

    @Override
    public Node getNode(Context context) {
        Node node = new Node();
        node.setModel(FundamentalGenerator.getModel(context, ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER),
                texture_id, model));
        node.getRelativeTransform().setPosition(c.getPos());
        node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(c.getRadius(), height, c.getRadius()));
        return node;
    }

    @Override
    public CollisionBox getBox() {
        return c;
    }

    @Override
    public MazeObject cloneFromData(String position, String size, int textureId) {
        float posX = Float.parseFloat(position.split(" ")[0]);
        float posY = Float.parseFloat(position.split(" ")[1]);
        float posZ = Float.parseFloat(position.split(" ")[2]);
        float sizeX = Float.parseFloat(size.split(" ")[0]);
        float sizeY = Float.parseFloat(size.split(" ")[1]);
        return new Obstacle(new Circle(new SFVertex3f(posX, posY, posZ), sizeX), sizeY, textureId, model);
    }
}

