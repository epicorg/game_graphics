package graphic.sfogl2;

import android.opengl.GLES20;


public class SFOGLFrameBuffer {

    private int frameBufferObject;

    public static int createFrameBuffer() {
        int[] fbo = new int[1];
        GLES20.glGenFramebuffers(1, fbo, 0);
        int frameBuffer = fbo[0];
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer);
        return frameBuffer;
    }

    public static void destroyFrameBuffer(int frameBufferObject) {
        int[] nfbo = new int[1];
        nfbo[0] = frameBufferObject;
        GLES20.glDeleteFramebuffers(1, nfbo, 0);
    }

    public static void checkFrameBuffer() {
        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Framebuffer is not complete");
            throw new RuntimeException();
        }
    }

    public void prepare() {
        this.frameBufferObject = createFrameBuffer();
    }

    public int getFrameBufferObject() {
        return frameBufferObject;
    }

    public void apply() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferObject);
    }

    public void unapply() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public void attachRenderBuffer(int attachment, SFOGLRenderBuffer renderBuffer) {
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, attachment, GLES20.GL_RENDERBUFFER, renderBuffer.getRenderBufferObject());
    }

    public void attachTexture(int attachment, SFOGLTexture2D texture) {
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, attachment, GLES20.GL_TEXTURE_2D, texture.getTextureObject(), 0);
    }

    public void attachCubeMap(int attachment, SFOGLCubeMap cubeMap, int index) {
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, attachment, SFOGLCubeMap.MAPS[index], cubeMap.getTextureObject(), 0);
    }

    public void destroy() {
        destroyFrameBuffer(frameBufferObject);
    }

    public void attachRenderBuffer(int attachment, int renderBuffer) {
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, attachment, GLES20.GL_RENDERBUFFER, renderBuffer);
    }

}
