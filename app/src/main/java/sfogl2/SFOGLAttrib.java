package sfogl2;

import android.opengl.GLES20;


public class SFOGLAttrib {

	private int attribLocation=-1;
	private String name;
	
	public SFOGLAttrib(String name) {
		super();
		this.name = name;
	}
	
	public void init(int shadingProgram){
		attribLocation = GLES20.glGetAttribLocation(shadingProgram, name);
	}
	
	public int getAttribLocation() {
		return attribLocation;
	}
	
	public void bindAttributef(int vertexBufferObject,int valueSize){
        GLES20.glEnableVertexAttribArray(attribLocation);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject);
        GLES20.glVertexAttribPointer(attribLocation, valueSize, GLES20.GL_FLOAT, false, 0, 0);
	}
}
