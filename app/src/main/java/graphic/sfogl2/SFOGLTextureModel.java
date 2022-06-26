package graphic.sfogl2;

import android.opengl.GLES20;

import java.util.ArrayList;

import graphic.shadow.graphics.SFImageFormat;

public class SFOGLTextureModel {

    private static ArrayList<SFOGLTextureModel> models = new ArrayList<SFOGLTextureModel>();
    private int textureWrapS;
    private int textureWrapT;
    private int minFilter;
    private int maxFilter;
    private int internalFormat;

    private SFOGLTextureModel(int internalFormat, int textureWrapS, int textureWrapT,
                              int minFilter, int maxFilter) {
        super();
        this.textureWrapS = textureWrapS;
        this.textureWrapT = textureWrapT;
        this.minFilter = minFilter;
        this.maxFilter = maxFilter;
        this.internalFormat = internalFormat;
    }

    public static int getInternalFormat(int model) {
        return models.get(model).internalFormat;
    }

    public static void applyModel(int target, int model) {
        models.get(model).generateTexturesParameters(target);
    }

    public static void clearAllModel() {
        models.clear();
    }

    public static int generateTextureObjectModel(SFImageFormat internalFormat, int textureWrapS, int textureWrapT,
                                                 int minFilter, int maxFilter) {
        SFOGLTextureModel textureModel = new SFOGLTextureModel(getInternalFormat(internalFormat), textureWrapS, textureWrapT, minFilter, maxFilter);
        models.add(textureModel);
        return models.size() - 1;
    }

    public static int getType(SFImageFormat format) {
        switch (format) {
            case ALPHA:
                return GLES20.GL_ALPHA;
            case GRAY:
                return GLES20.GL_LUMINANCE;
            case GRAY_ALPHA:
                return GLES20.GL_LUMINANCE_ALPHA;
            case RGB:
            case RGB565:
                return GLES20.GL_RGB;
            case RGBA:
            case RGBA4:
            case RGBA5551:
                return GLES20.GL_RGBA;
            default:
                return GLES20.GL_RGBA;

        }
    }

    public static int getInternalFormat(SFImageFormat format) {
        switch (format) {
            case ALPHA:
                return GLES20.GL_ALPHA;
            case GRAY:
                return GLES20.GL_LUMINANCE;
            case GRAY_ALPHA:
                return GLES20.GL_LUMINANCE_ALPHA;
            case RGB:
            case RGB565:
                return GLES20.GL_RGB;
            case RGBA:
            case RGBA4:
            case RGBA5551:
                return GLES20.GL_RGBA;
            default:
                return GLES20.GL_RGBA;
        }
    }

    public static int getFormat(SFImageFormat format) {
        switch (format) {
            case ALPHA:
            case GRAY:
            case GRAY_ALPHA:
            case RGB:
            case RGBA:
                return GLES20.GL_UNSIGNED_BYTE;
            case RGB565:
                return GLES20.GL_UNSIGNED_SHORT_5_6_5;
            case RGBA4:
                return GLES20.GL_UNSIGNED_SHORT_4_4_4_4;
            case RGBA5551:
                return GLES20.GL_UNSIGNED_SHORT_5_5_5_1;
            default:
                return GLES20.GL_RGBA;
        }
    }

    public void generateTexturesParameters(int target) {
        setupTextureParameters(target, textureWrapS, textureWrapT,
                minFilter, maxFilter);
    }

    private void setupTextureParameters(int target, int textureWrapS, int textureWrapT, int minFilter, int maxFilter) {
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MIN_FILTER, minFilter);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MAG_FILTER, maxFilter);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_S, textureWrapS);
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_T, textureWrapT);
    }

}
