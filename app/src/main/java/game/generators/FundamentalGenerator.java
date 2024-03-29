package game.generators;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import game.graphics.MaterialKeeper;
import game.graphics.ModelKeeper;
import graphic.integration.ArrayObject;
import graphic.integration.BitmapTexture;
import graphic.integration.Material;
import graphic.integration.Mesh;
import graphic.integration.Model;
import graphic.integration.Node;
import graphic.integration.ShadingProgram;
import graphic.sfogl2.SFOGLTextureModel;
import graphic.shadow.graphics.SFImageFormat;

/**
 * Utility class with static methods which are used to generate {@link Model}and {@link Node}.
 *
 * @author Torlaschi
 * @see Material
 * @see ArrayObject
 * @see ShadingProgram
 */
public class FundamentalGenerator {

    /**
     * Builds a <code>Model</code> from a file with a texture.
     *
     * @param context   <code>Context</code> from which to load the file
     * @param program   <code>Shader</code> to use in the <code>Material</code> of the Model
     * @param textureId ID of the texture to use for the <code>Material</code> of the Model
     * @param obj       name of the file that contains the <code>Model</code> geometry
     * @return the generated <code>Model</code>.
     */
    public static Model getModel(Context context, ShadingProgram program, int textureId, String obj) {
        return getModelFromFileAndMaterial(context, MaterialKeeper.MATERIAL_KEEPER.getMaterial(context, program, textureId), obj);
    }

    /**
     * Builds a <code>Model</code> from a file with a specified <code>Material</code>.
     *
     * @param context  <code>Context</code> from which resources can be loaded
     * @param material <code>Material</code> with which the <code>Model</code> is built
     * @param obj      name of the file which contains <code>Model</code>'s geometry
     */
    public static Model getModelFromFileAndMaterial(Context context, Material material, String obj) {
        return ModelKeeper.MODEL_KEEPER.getModel(context, obj, material);
    }

    /**
     * Builds a <code>Node</code> from an <code>ArrayObject</code> and a <code>Bitmap</code> as texture.
     *
     * @param arrayObject <code>ArrayObject</code> from which the <code>Model</code>'s geometry of the <code>Node</code> is get
     * @param bitmap      <code>Bitmap</code> to be used as texture for <code>Node</code> Material
     * @param program     <code>ShadingProgram</code> with who Node <code>Model</code>'s Material is built
     */
    public static Node generateNode(ArrayObject arrayObject, Bitmap bitmap, ShadingProgram program) {
        Node nodePos = new Node();
        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGBA, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_CLAMP_TO_EDGE, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture bitmapTexture = BitmapTexture.loadBitmapTexture(bitmap, textureModel);
        bitmapTexture.init();

        Material material = new Material(program);
        material.getTextures().add(bitmapTexture);
        nodePos.setModel(getModelFromArrayObjectAndMaterial(arrayObject, material));

        return nodePos;
    }

    private static Model getModelFromArrayObjectAndMaterial(ArrayObject arrayObject, Material material) {
        Mesh meshPos = new Mesh(arrayObject);
        meshPos.init();
        Model modelPos = new Model();
        modelPos.setRootGeometry(meshPos);
        modelPos.setMaterialComponent(material);
        return modelPos;
    }

}
