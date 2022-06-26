package graphic.sfogl2;

import android.opengl.GLES20;

public class SFOGLSystemState {

    public static void cleanupColorAndDepth(float red, float green, float blue, float alpha) {
        GLES20.glClearColor(red, green, blue, alpha);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

}
