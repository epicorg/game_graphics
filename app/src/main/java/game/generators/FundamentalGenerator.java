package game.generators;

import android.content.Context;

import game.graphics.TextureKeeper;
import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.BitmapTexture;
import sfogl.integration.Material;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.ShadingProgram;

/**
 * Created by Andrea on 27/03/2015.
 */
public class FundamentalGenerator {

    public static Material getMaterial(Context context, ShadingProgram program, int textureId) {
        BitmapTexture texture = TextureKeeper.getTexture(context, textureId);
        texture.init();
        Material material = new Material(program);
        material.getTextures().add(texture);

        return material;
    }

    public static Model getModel(Context context, ShadingProgram program, int texture_id, String obj) {
        ArrayObject[] object = ObjLoader.arrayObjectFromFile(context, obj);
        Mesh mesh = new Mesh(object[0]);
        mesh.init();
        Material material = getMaterial(context, program, texture_id);
        Model model = new Model();
        model.setRootGeometry(mesh);
        model.setMaterialComponent(material);

        return model;
    }

}
