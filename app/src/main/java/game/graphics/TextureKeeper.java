package game.graphics;

import android.content.Context;
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

    private static HashMap<Integer, BitmapTexture> map = new HashMap<Integer, BitmapTexture>();

    public static BitmapTexture getTexture(Context context, int texture_id) {
        if (map.containsKey(texture_id))
            return map.get(texture_id);
        else {
            int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
            BitmapTexture tex = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(), texture_id), textureModel);
            tex.init();
            Log.d(LOG_TAG, "Loaded Texture: " + texture_id);
            map.put(texture_id, tex);
            return tex;
        }
    }

}
