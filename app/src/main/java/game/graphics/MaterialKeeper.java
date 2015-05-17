package game.graphics;

import android.content.Context;

import java.util.HashMap;

import sfogl.integration.BitmapTexture;
import sfogl.integration.Material;
import sfogl.integration.ShadingProgram;

/**
 * Created by Andrea on 17/05/2015.
 */
public class MaterialKeeper {

    public static final String LOG_TAG = "MaterialKeeper";

    public static MaterialKeeper instance = new MaterialKeeper();

    private HashMap<Integer, Material> mapFromResources = new HashMap<>();
    private HashMap<Integer, Material> mapFromColors = new HashMap<>();

    private MaterialKeeper() {
    }

    public static MaterialKeeper getInstance() {
        return instance;
    }

    public Material getMaterial(Context context, ShadingProgram program, int textureId) {
        if (mapFromResources.containsKey(textureId))
            return mapFromResources.get(textureId);
        else {
            BitmapTexture texture = TextureKeeper.getInstance().getTexture(context, textureId);
            Material mat = new Material(program);
            mat.getTextures().add(texture);
            mapFromResources.put(textureId, mat);
            return mat;
        }
    }

    public Material getColorMaterial(Context context, ShadingProgram program, int color) {
        if (mapFromColors.containsKey(color))
            return mapFromColors.get(color);
        else {
            BitmapTexture texture = TextureKeeper.getInstance().getColorTexture(context, color);
            Material mat = new Material(program);
            mat.getTextures().add(texture);
            mapFromColors.put(color, mat);
            return mat;
        }
    }

    public void clear() {
        mapFromResources.clear();
        mapFromColors.clear();
    }

}
