package game.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import java.util.HashMap;

import sfogl.integration.BitmapTexture;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFImageFormat;

/**
 * Created by depa on 29/03/15.
 */
public class TextureKeeper {

    public static final String LOG_TAG = "TextureKeeper";

    private static HashMap<Integer, BitmapTexture> mapFromResources = new HashMap<Integer, BitmapTexture>();
    private static HashMap<Integer, BitmapTexture> mapFromColors = new HashMap<Integer, BitmapTexture>();

    public static BitmapTexture getTexture(Context context, int textureId) {
        if (mapFromResources.containsKey(textureId))
            return mapFromResources.get(textureId);
        else {
            BitmapTexture tex = getBitmapTextureFromResource(context, textureId);
            Log.d(LOG_TAG, "Loaded Texture: " + textureId);
            mapFromResources.put(textureId, tex);
            return tex;
        }
    }

    public static BitmapTexture getColorTexture(Context context, int color) {
        if (mapFromColors.containsKey(color))
            return mapFromColors.get(color);
        else {
            BitmapTexture tex = getBitmapTextureFromColor(color);
            Log.d(LOG_TAG, "Loaded ColorTexture: " + color);
            mapFromColors.put(color, tex);
            return tex;
        }
    }

    public static void reload(Context context) {
        for (int i : mapFromResources.keySet()) {
            BitmapTexture tex = getBitmapTextureFromResource(context, i);
            Log.d(LOG_TAG, "Reloaded Texture: " + i);
            mapFromResources.put(i, tex);
        }

        for (int color : mapFromColors.keySet()) {
            BitmapTexture tex = getBitmapTextureFromColor(color);
            Log.d(LOG_TAG, "Reloaded ColorTexture: " + color);
            mapFromColors.put(color, tex);
        }
    }

    private static BitmapTexture getBitmapTextureFromResource(Context context, int textureId) {
        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture tex = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(), textureId), textureModel);
        tex.init();
        return tex;
    }

    private static BitmapTexture getBitmapTextureFromColor(int color) {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(64, 64, conf);
        bitmap.eraseColor(color);

        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture tex = BitmapTexture.loadBitmapTexture(bitmap, textureModel);
        tex.init();
        return tex;
    }

}
