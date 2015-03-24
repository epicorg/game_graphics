package sfogl2;

import android.opengl.GLES20;

public class SFOGLRenderBuffer {

	private int renderBufferObject;
	
	public void setup(int width, int height){
		this.renderBufferObject=createRenderBuffer( width, height);
	}
	
	public int getRenderBufferObject() {
		return renderBufferObject;
	}

	public void destroy(){
		destroyRenderBuffer(renderBufferObject);
	}

	public static int createRenderBuffer(int width, int height) {
		//Create a RenderBuffer to Be Used As Depth Buffer in the FrameBufferObject
		int rbo[]=new int[1];
        GLES20.glGenRenderbuffers(1, rbo, 0);
		int rb=rbo[0];
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, rb);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
		return rb;
	}
	
	public static void destroyRenderBuffer(int renderBufferObject) {
		int[] textures = { renderBufferObject };
        GLES20.glDeleteRenderbuffers(GLES20.GL_TEXTURE_2D, textures, 1);
	}
}
