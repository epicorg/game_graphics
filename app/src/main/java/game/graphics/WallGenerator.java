package game.graphics;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import graphic.integration.ArrayObject;
import graphic.integration.Mesh;
import graphic.integration.Model;
import graphic.integration.ShadingProgram;

/**
 * Utility class to generate a {@link Model} for parallelepiped with proportional texture.
 *
 * @author De Pace
 * @see ShadingProgram
 */
public class WallGenerator {

    private static final String LOG = WallGenerator.class.getSimpleName();

    private final float w, h, l;
    private final int texture;
    private final Context context;
    private final ShadingProgram program;

    private final HashMap<String, Mesh> meshMap;
    private final HashMap<String, Model> modelMap;

    /**
     * Creates a new WallGenerator with the parameters to build a parallelepiped.
     *
     * @param context <code>Context</code> to find resources
     * @param program <code>ShadingProgram</code> to obtain the shader for the texture
     * @param texture texture index
     * @param w       x dimension of the parallelepiped
     * @param h       y dimension of the parallelepiped
     * @param l       z dimension of the parallelepiped
     */
    public WallGenerator(Context context, ShadingProgram program, int texture, float w, float h, float l) {
        this.program = program;
        this.context = context;
        this.w = w / 2;
        this.h = h / 2;
        this.l = l / 2;
        this.texture = texture;

        meshMap = new HashMap<>();
        modelMap = new HashMap<>();
    }

    /**
     * @return Parallelepiped <code>Model</code>.
     */
    public Model getModel() {
        String dimensions = w + "," + h + "," + l;
        Log.d(LOG, "New WallModel:");

        Mesh mesh;
        if ((mesh = meshMap.get(dimensions)) == null) {
            mesh = new Mesh(generateModel());
            mesh.init();
            meshMap.put(dimensions, mesh);
        }

        String textureAndDimensions = texture + "," + dimensions;
        Log.d(LOG, "TextureAndDimensions: " + textureAndDimensions);

        Model model;
        if ((model = modelMap.get(textureAndDimensions)) == null) {
            model = new Model();
            model.setRootGeometry(mesh);
            model.setMaterialComponent(MaterialKeeper.MATERIAL_KEEPER.getMaterial(context, program, texture));
            modelMap.put(textureAndDimensions, model);
        }

        return model;
    }

    private ArrayObject generateModel() {
        float hh = 2 * h;
        return new ArrayObject(
                new float[]{
                        +w, 0, -l,
                        +w, 0, +l,
                        +w, +hh, -l,
                        +w, +hh, +l,

                        +w, +hh, +l,
                        -w, +hh, +l,
                        +w, +hh, -l,
                        -w, +hh, -l,

                        -w, 0, +l,
                        -w, +hh, +l,
                        +w, 0, +l,
                        +w, +hh, +l,

                        -w, +hh, +l,
                        -w, 0, +l,
                        -w, +hh, -l,
                        -w, 0, -l,

                        +w, 0, -l,
                        +w, +hh, -l,
                        -w, 0, -l,
                        -w, +hh, -l
                },
                new float[]{
                        -1, 0, 0
                },
                new float[]{
                        0, 0, 0,
                        l, 0, 0,
                        0, h, 0,
                        l, h, 0,

                        w, l, 0,
                        0, l, 0,
                        w, 0, 0,
                        0, 0, 0,

                        w, 0, 0,
                        w, h, 0,
                        0, 0, 0,
                        0, h, 0,

                        0, h, 0,
                        0, 0, 0,
                        l, h, 0,
                        l, 0, 0,

                        w, 0, 0,
                        w, h, 0,
                        0, 0, 0,
                        0, h, 0
                },
                new short[]{
                        3, 0, 2, 0, 3, 1,
                        7, 4, 6, 4, 7, 5,
                        11, 8, 10, 8, 11, 9,
                        15, 12, 14, 12, 15, 13,
                        19, 16, 18, 16, 19, 17
                });
    }

}
