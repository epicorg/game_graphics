package game.generators;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import game.graphics.TextureKeeper;
import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.BitmapTexture;
import sfogl.integration.Material;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFImageFormat;

/**
 * Created by Andrea on 27/03/2015.
 */
public class FundamentalGenerator {

    public static Material getMaterial(Context context, ShadingProgram program, int textureId) {
        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture texture = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(), textureId), textureModel);
        texture.init();
        Material material = new Material(program);
        material.getTextures().add(texture);

        return material;
    }

    public static Model getModel(Context context, ShadingProgram program, int texture_id, String obj) {
        BitmapTexture texture = TextureKeeper.getTexture(context, texture_id);
        ArrayObject[] object = ObjLoader.arrayObjectFromFile(context, obj);
        Mesh mesh = new Mesh(object[0]);
        mesh.init();
        Material material = new Material(program);
        material.getTextures().add(texture);
        Model model = new Model();
        model.setRootGeometry(mesh);
        model.setMaterialComponent(material);
        return model;
    }

}
