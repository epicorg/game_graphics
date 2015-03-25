package sfogl2;

import android.opengl.GLES20;

public class SFOGLRenderedCubeMap {

    private int sizeX, sizeY;
    private int useDepth;

    private SFOGLCubeMap texture = new SFOGLCubeMap();
    private SFOGLRenderBuffer renderBuffer = new SFOGLRenderBuffer();
    private SFOGLFrameBuffer frameBuffer = new SFOGLFrameBuffer();

    public SFOGLRenderedCubeMap() {

    }

    public SFOGLCubeMap getTexture() {
        return texture;
    }

    public SFOGLRenderedCubeMap(int sizeX, int sizeY, boolean useDepth) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.useDepth = useDepth ? 1 : 0;
    }

    public int getTextureObject() {
        return this.texture.getTextureObject();
    }

    public void applyTexture(int textureLevel) {
        texture.apply(textureLevel);
    }

    public void destroy() {
        texture.delete();
        boolean useDepth = ((this.useDepth & 1) == 1);
        if (useDepth)
            renderBuffer.destroy();
        frameBuffer.destroy();
    }

    public void prepare() {
        if (texture.getTextureObject() == SFOGLTexture.NULL_OBJECT) {
            texture.setup(sizeX, sizeY);
            boolean useDepth = ((this.useDepth & 1) == 1);
            if (useDepth)
                renderBuffer.setup(sizeX, sizeY);
            frameBuffer.prepare();
            frameBuffer.apply();
            if (useDepth)
                frameBuffer.attachRenderBuffer(GLES20.GL_DEPTH_ATTACHMENT, renderBuffer);
            SFOGLFrameBuffer.checkFrameBuffer();
            frameBuffer.unapply();
        }
    }

    public void apply(int index) {
        frameBuffer.apply();
        frameBuffer.attachCubeMap(GLES20.GL_COLOR_ATTACHMENT0, texture, index);
        GLES20.glClearColor(1, 1, 1, 1);
        boolean useDepth = ((this.useDepth & 1) == 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | ((useDepth) ? GLES20.GL_DEPTH_BUFFER_BIT : 0));
        if (!useDepth) {
            GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        }
        GLES20.glViewport(0, 0, sizeX, sizeY);
    }

    public void unapply() {
        frameBuffer.unapply();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }
}
