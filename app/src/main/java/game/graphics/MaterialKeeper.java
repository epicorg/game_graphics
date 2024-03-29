package game.graphics;

import android.content.Context;

import java.util.HashMap;

import graphic.integration.BitmapTexture;
import graphic.integration.Material;
import graphic.integration.ShadingProgram;

/**
 * Singleton that manages the {@link Material} loaded from files. It allows to access the
 * {@link Material}without load them several times or automatically load them when needed.
 *
 * @author Torlaschi
 * @date 17/05/2015
 */

public enum MaterialKeeper {

    MATERIAL_KEEPER;

    private final HashMap<Integer, Material> mapFromResources = new HashMap<>();
    private final HashMap<Integer, Material> mapFromColors = new HashMap<>();

    public Material getMaterial(Context context, ShadingProgram program, int textureId) {
        if (mapFromResources.containsKey(textureId))
            return mapFromResources.get(textureId);
        else {
            BitmapTexture texture = TextureKeeper.TEXTURE_KEEPER.getTexture(context, textureId);
            Material material = new Material(program);
            material.getTextures().add(texture);
            mapFromResources.put(textureId, material);
            return material;
        }
    }

    public Material getColorMaterial(ShadingProgram program, int color) {
        if (mapFromColors.containsKey(color))
            return mapFromColors.get(color);
        else {
            BitmapTexture texture = TextureKeeper.TEXTURE_KEEPER.getColorTexture(color);
            Material material = new Material(program);
            material.getTextures().add(texture);
            mapFromColors.put(color, material);
            return material;
        }
    }

    public void clear() {
        mapFromResources.clear();
        mapFromColors.clear();
    }

}
