package sfogl2;

import android.opengl.GLES20;

import shadow.graphics.SFBitmap;

public class SFOGLTexture2D extends SFOGLTexture {

    public SFOGLTexture2D() {
        super();
    }

    public SFOGLTexture2D(int textureModel) {
        super(textureModel);
    }

    public static void destroyTexture(GLES20 gl, int textureObject) {
        if (textureObject != -1) {
            int[] textures = {textureObject};
            GLES20.glDeleteTextures(GLES20.GL_TEXTURE_2D, textures, 0);
        }
    }

    public static int createTexture2D(int textureModel, int width, int height) {
        int tx = generateTexture();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tx);
        SFOGLTextureModel.applyModel(GLES20.GL_TEXTURE_2D, textureModel);
        int internalFormat = SFOGLTextureModel.getInternalFormat(textureModel);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return tx;
    }

    public static int createTexture2D(int textureModel, SFBitmap bitmap) {
        int tx = generateTexture();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tx);
        SFOGLTextureModel.applyModel(GLES20.GL_TEXTURE_2D, textureModel);
        int internalFormat = SFOGLTextureModel.getInternalFormat(bitmap.getFormat());
        int type = SFOGLTextureModel.getType(bitmap.getFormat());
        int format = SFOGLTextureModel.getFormat(bitmap.getFormat());

//		System.err.println("internalFormat "+internalFormat+" "+GLES20.GL_RGB);
//		System.err.println("type "+type+" "+GLES20.GL_RGB);
//		System.err.println("format "+format+" "+GLES20.GL_UNSIGNED_BYTE);
//		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB,
//				bitmap.getWidth(), bitmap.getHeight(), 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, (ByteBuffer)bitmap.getData());

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, internalFormat, bitmap.getWidth(), bitmap.getHeight(), 0, type, format, bitmap.getData());
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return tx;
    }

    public void apply(int textureLevel) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureLevel);
        //glEnable(GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObject);
    }

    public void bind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObject);
    }

    public void setup(int width, int height) {
        this.textureObject = createTexture2D(textureModel, width, height);
    }

    public void setup(SFBitmap bitmap) {
        this.textureObject = createTexture2D(textureModel, bitmap);
    }

    public void generateMipmap(GLES20 gl) {
        generateMipmap(GLES20.GL_TEXTURE_2D, textureObject);
    }

    public void destroy(GLES20 gl) {
        destroyTexture(gl, textureObject);
    }
}
