package generators;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

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
 * Created by Andrea on 26/03/2015.
 */
public class ModelGenerator {

    private Context context;
    private ShadingProgram program;
    private Model model;

    public ModelGenerator(Context context, ShadingProgram program, int textureId, String obj) {
        this.context = context;
        this.program = program;

        setup(textureId, obj);
    }

    private void setup(int textureId, String obj) {
        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture texture = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(), textureId), textureModel);
        texture.init();
        ArrayObject[] object = ObjLoader.arrayObjectFromFile(context, obj);
        Mesh mesh = new Mesh(object[0]);
        mesh.init();
        Material material = new Material(program);
        material.getTextures().add(texture);
        model = new Model();
        model.setRootGeometry(mesh);
        model.setMaterialComponent(material);
    }

    public Model getModel() {
        return model;
    }
}
