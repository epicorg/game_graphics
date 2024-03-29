package game.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import java.util.HashMap;

import graphic.integration.BitmapTexture;
import graphic.sfogl2.SFOGLTextureModel;
import graphic.shadow.graphics.SFImageFormat;

/**
 * It manages the texture loaded from resources. It allows to access the texture as
 * {@link BitmapTexture} without load them several times or automatically load them when needed.
 *
 * @author De Pace
 */
public enum TextureKeeper {

    TEXTURE_KEEPER;

    private static final String LOG_TAG = "TextureKeeper";

    private final HashMap<Integer, BitmapTexture> mapFromResources = new HashMap<>();
    private final HashMap<Integer, BitmapTexture> mapFromColors = new HashMap<>();

    /**
     * Loads a new texture from an image in the resources or return the BitmapTexture if the image
     * has already been loaded.
     *
     * @param context   <code>Context</code> to find resources
     * @param textureId image index in the resources
     * @return <code>BitmapTexture</code> which represents the loaded texture
     */
    public BitmapTexture getTexture(Context context, int textureId) {
        if (mapFromResources.containsKey(textureId))
            return mapFromResources.get(textureId);
        else
            return loadTextureFromId(context, textureId, "Loaded Texture: " + textureId);
    }

    /**
     * Creates a texture from a color and stores it. Represents a uniform color.
     *
     * @param color color from which the texture is obtained
     * @return <code>BitmapTexture</code> which represents the loaded texture
     */
    public BitmapTexture getColorTexture(int color) {
        if (mapFromColors.containsKey(color))
            return mapFromColors.get(color);
        else
            return loadColorTexture(color, "Loaded ColorTexture: " + color);
    }

    /**
     * Loads every loaded image, when needed.
     *
     * @param context <code>Context</code> to find resources
     */
    public void reload(Context context) {
        for (int texture : mapFromResources.keySet())
            loadTextureFromId(context, texture, "Reloaded Texture: " + texture);
        for (int color : mapFromColors.keySet())
            loadColorTexture(color, "Reloaded ColorTexture: " + color);
    }

    private BitmapTexture loadColorTexture(int color, String message) {
        BitmapTexture tex = getBitmapTextureFromColor(color);

        if (message.length() > 0)
            Log.d(LOG_TAG, message);

        mapFromColors.put(color, tex);
        return tex;
    }

    private BitmapTexture loadTextureFromId(Context context, int textureId, String message) {
        BitmapTexture tex = getBitmapTextureFromResource(context, textureId);

        if (message.length() > 0)
            Log.d(LOG_TAG, message);

        mapFromResources.put(textureId, tex);
        return tex;
    }

    private BitmapTexture getBitmapTextureFromResource(Context context, int textureId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), textureId);
        return getBitmapTextureFromBitmap(bitmap);
    }

    private BitmapTexture getBitmapTextureFromColor(int color) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(64, 64, conf);
        bitmap.eraseColor(color);

        return getBitmapTextureFromBitmap(bitmap);
    }

    private BitmapTexture getBitmapTextureFromBitmap(Bitmap bitmap) {
        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGBA, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture tex = BitmapTexture.loadBitmapTexture(bitmap, textureModel);
        tex.init();
        return tex;
    }

    public void clear() {
        mapFromResources.clear();
        mapFromColors.clear();
    }

}
