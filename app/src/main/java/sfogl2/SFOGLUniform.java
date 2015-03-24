package sfogl2;

import android.opengl.GLES20;

public class SFOGLUniform {

	private int uniformLocation=-1;
	private String name;
	private float[] value;
	private int[] intValue;
	
	public SFOGLUniform(String name) {
		super();
		this.name = name;
	}
	
	public float[] getValue() {
		return value;
	}
	
	public int getUniformLocation() {
		return uniformLocation;
	}
	
	public void init(int shadingProgram){
		uniformLocation = GLES20.glGetUniformLocation(shadingProgram, name);
	}

	public void setValue(float... value) {
		this.value = value;
		if(uniformLocation!=-1){
			switch (value.length) {
				case 1: GLES20.glUniform1fv(uniformLocation, 1, value, 0);break;
				case 2: GLES20.glUniform2fv(uniformLocation, 1, value, 0);break;
				case 3: GLES20.glUniform3fv(uniformLocation, 1, value, 0);break;
				case 4: GLES20.glUniform4fv(uniformLocation, 1, value, 0);break;
				case 9: GLES20.glUniformMatrix3fv(uniformLocation, 1, false, value, 0);break;
				case 16: GLES20.glUniformMatrix4fv(uniformLocation, 1, false, value, 0);break;
				default:
			}
		}
	}
	
	public void setValuei(int... value) {
		this.intValue = value;
		if(uniformLocation!=-1){
			switch (value.length) {
				case 1: GLES20.glUniform1iv(uniformLocation, 1, value, 0);break;
				case 2: GLES20.glUniform2iv(uniformLocation, 1, value, 0);break;
				case 3: GLES20.glUniform3iv(uniformLocation, 1, value, 0);break;
				case 4: GLES20.glUniform4iv(uniformLocation, 1, value, 0);break;
				default:
			}
		}
	}
}
