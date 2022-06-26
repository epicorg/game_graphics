package graphic.sfogl2;

import android.opengl.GLES20;

public class SFOGLRenderedTexture {

    private int sizeX, sizeY;
    private int useDepth;

    private SFOGLTexture2D texture = new SFOGLTexture2D();
    private SFOGLRenderBuffer renderBuffer = new SFOGLRenderBuffer();
    private SFOGLFrameBuffer frameBuffer = new SFOGLFrameBuffer();

    public SFOGLRenderedTexture() {
    }

    public SFOGLRenderedTexture(int sizeX, int sizeY, boolean useDepth) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.useDepth = useDepth ? 1 : 0;
    }

    public SFOGLRenderedTexture(int sizeX, int sizeY, boolean useDepth, int textureModel) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.useDepth = useDepth ? 1 : 0;
    }

    public SFOGLTexture2D getTexture() {
        return texture;
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
            frameBuffer.attachTexture(GLES20.GL_COLOR_ATTACHMENT0, texture);
            SFOGLFrameBuffer.checkFrameBuffer();
            frameBuffer.unapply();
        }
    }

    public void apply() {
        frameBuffer.apply();
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
