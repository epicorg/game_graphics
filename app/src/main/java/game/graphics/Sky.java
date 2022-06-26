package game.graphics;

import android.content.Context;
import android.graphics.BitmapFactory;

import epic.org.R;

import java.util.List;

import game.generators.FundamentalGenerator;
import graphic.integration.ArrayObject;
import graphic.integration.Node;
import graphic.integration.ShadingProgram;
import graphic.shadow.math.SFMatrix3f;
import graphic.shadow.math.SFTransform3f;
import graphic.shadow.math.SFVertex3f;

/**
 * Represents a skybox.
 *
 * @author Torlaschi
 * @date 31/03/2015
 * @see ShadingProgram
 * @see Node
 */
public class Sky {

    private static final int SKY_DISTANCE = 100;
    private static final short[] indices = new short[]{3, 2, 0, 0, 1, 3};

    private final ArrayObject arrayObjectPosX = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE
            },
            new float[]{
                    -1, 0, 0
            },
            new float[]{
                    0, 0, 0,
                    1, 0, 0,
                    0, 1, 0,
                    1, 1, 0
            },
            indices
    );
    private final ArrayObject arrayObjectPosY = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE

            },
            new float[]{
                    0, -1, 0,
            },
            new float[]{
                    1, 1, 0,
                    0, 1, 0,
                    1, 0, 0,
                    0, 0, 0
            },
            indices
    );
    private final ArrayObject arrayObjectPosZ = new ArrayObject(
            new float[]{
                    -SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE
            },
            new float[]{
                    0, 0, -1
            },
            new float[]{
                    1, 0, 0,
                    1, 1, 0,
                    0, 0, 0,
                    0, 1, 0
            },
            indices
    );
    private final ArrayObject arrayObjectNegX = new ArrayObject(
            new float[]{
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE
            },
            new float[]{
                    +1, 0, 0
            },
            new float[]{
                    0, 1, 0,
                    0, 0, 0,
                    1, 1, 0,
                    1, 0, 0
            },
            indices
    );
    private final ArrayObject arrayObjectNegZ = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE
            },
            new float[]{
                    0, 0, +1
            },
            new float[]{
                    1, 0, 0,
                    1, 1, 0,
                    0, 0, 0,
                    0, 1, 0
            },
            indices
    );
    private final Context context;
    private final ShadingProgram program;
    private final SFVertex3f position;
    private final Node mainNode = new Node();

    /**
     * Creates a new <code>Sky</code> cube centered in the given position.
     *
     * @param context  <code>Context</code> from which to retrieve the resources.
     * @param program  <code>ShadingProgram</code> to use for drawing the <code>Sky</code>.
     * @param position position of the center of the <code>Sky</code>;
     */
    public Sky(Context context, ShadingProgram program, SFVertex3f position) {
        this.program = program;
        this.context = context;
        this.position = position;

        setup();
    }

    private void setup() {
        List<Node> list = mainNode.getSonNodes();
        list.add(generateNode(arrayObjectPosX, R.drawable.skybox_posx));
        list.add(generateNode(arrayObjectPosY, R.drawable.skybox_posy));
        list.add(generateNode(arrayObjectPosZ, R.drawable.skybox_negz));
        list.add(generateNode(arrayObjectNegX, R.drawable.skybox_negx));
        list.add(generateNode(arrayObjectNegZ, R.drawable.skybox_posz));

        mainNode.getRelativeTransform().setPosition(0, 0, 0);
        mainNode.getRelativeTransform().setMatrix(SFMatrix3f.getIdentity());
    }

    private Node generateNode(ArrayObject arrayObject, int textureId) {
        return FundamentalGenerator.generateNode(arrayObject,
                BitmapFactory.decodeResource(context.getResources(), textureId), program);
    }

    /**
     * Draws the <code>Sky</code>.
     */
    public void draw() {
        mainNode.getRelativeTransform().setPosition(position.getX(), position.getY(), position.getZ());
        mainNode.updateTree(new SFTransform3f());
        mainNode.draw();
    }

}
