package sfogl2;

import android.opengl.GLES20;

import sfogl2.SFOGLTexture;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFBitmap;

public class SFOGLCubeMap extends SFOGLTexture {
	
	public static final int[] MAPS={
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
            GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
            GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z,
	};
	
	public SFOGLCubeMap( ) {
		super();
	}
	
	public SFOGLCubeMap(int textureModel) {
		super(textureModel);
	}

	public void destroy(){
		destroyTexture(textureObject);
	}
	
	
	public static void destroyTexture(int textureObject){
		if(textureObject!=-1){
			int[] textures={textureObject};
            GLES20.glDeleteTextures(GLES20.GL_TEXTURE_CUBE_MAP, textures, 0);
		}
	}
	
	public void generateMipmap() {
		generateMipmap(GLES20.GL_TEXTURE_CUBE_MAP,textureObject);
	}
	
	public void bind(){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureObject);
	}
	
	public void setup(int width, int height){
		this.textureObject=createTextureCuberMap(textureModel, width, height);
	}
	

	public void setup( SFBitmap[] bitmap){
		this.textureObject=createTextureCuberMap(textureModel, bitmap);
	}
	
	public int getTextureObject() {
		return textureObject;
	}
	
	public static void generateMipmap( int textureObject) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureObject);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
	}
	
	public static int createTextureCuberMap(int width, int height) {
		return createTextureCuberMap(-1, width, height);
	}

	public static int createTextureCuberMap(int textureModel, int width, int height) {
		int tx = generateTexture();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, tx);
		SFOGLTextureModel.applyModel(GLES20.GL_TEXTURE_2D, textureModel);
		for (int i = 0; i < MAPS.length; i++) {
            GLES20.glTexImage2D(MAPS[i], 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
		}
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
		return tx;
	}

	public static int createTextureCuberMap( int[] width, int[] height) {
		return createTextureCuberMap(-1,width,height);
	}
	
	public static int createTextureCuberMap( int textureModel, int[] width, int[] height) {
		int tx = generateTexture();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, tx);
		SFOGLTextureModel.applyModel(GLES20.GL_TEXTURE_2D,textureModel);
		for (int i = 0; i < MAPS.length; i++) {
            GLES20.glTexImage2D(MAPS[i], 0, GLES20.GL_RGBA, width[i], height[i], 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
		}
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
		return tx;
	}
	
	public static int createTextureCuberMap(int textureModel, SFBitmap[] bitmap) {
		int tx = generateTexture();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, tx);
		SFOGLTextureModel.applyModel(GLES20.GL_TEXTURE_2D,textureModel);
		int internalFormat = SFOGLTextureModel.getInternalFormat(textureModel);
		for (int i = 0; i < MAPS.length; i++) {
			int width=bitmap[i].getWidth();
			int height=bitmap[i].getHeight();
			int type=SFOGLTextureModel.getType(bitmap[i].getFormat());
			int format=SFOGLTextureModel.getType(bitmap[i].getFormat());
            GLES20.glTexImage2D(MAPS[i], 0, internalFormat, width, height, 0, type, format, bitmap[i].getData());
		}
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, 0);
		return tx;
	}
}
