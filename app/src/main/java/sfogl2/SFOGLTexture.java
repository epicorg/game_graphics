package sfogl2;

import android.opengl.GLES20;


public class SFOGLTexture {

    public static final int NULL_OBJECT = -1;

    protected int textureObject = NULL_OBJECT;
    protected int textureModel = 0;

    public SFOGLTexture(int textureModel) {
        this.textureModel = textureModel;
    }

    public SFOGLTexture() {
    }

    public static void generateMipmap(int target, int textureObject) {
        GLES20.glBindTexture(target, textureObject);
        GLES20.glGenerateMipmap(target);
        GLES20.glBindTexture(target, 0);
    }

    public static int generateTexture() {
        int txo[] = new int[1];
        GLES20.glGenTextures(1, txo, 0);
        int tx = txo[0];
        return tx;
    }

    public void delete() {
        int txo[] = new int[1];
        txo[0] = textureObject;
        GLES20.glDeleteTextures(1, txo, 0);
    }

    public void apply(int textureLevel) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureLevel);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObject);
    }

    public int getTextureObject() {
        return textureObject;
    }

    public int getTextureModel() {
        return textureModel;
    }

    public void setTextureModel(int textureModel) {
        this.textureModel = textureModel;
    }

    public void setupParameters() {
        SFOGLTextureModel.applyModel(GLES20.GL_TEXTURE_2D, textureModel);
    }
}