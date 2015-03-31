package sfogl2;

import android.opengl.GLES20;
import android.util.Log;

import java.io.PrintStream;


public class SFOGLShader {

    private String fragmentShader;
    private String vertexShader;
    private SFOGLAttrib[] attribs;
    private SFOGLUniform[] uniforms;

    private int shadingProgram, vShader, fShader;
    private boolean initialized = false;

    public SFOGLShader() {
        super();
    }

    public void setAttribs(String... attribs) {
        this.attribs = new SFOGLAttrib[attribs.length];
        for (int i = 0; i < attribs.length; i++) {
            this.attribs[i] = new SFOGLAttrib(attribs[i]);
        }
    }

    public void setUniforms(String... uniforms) {
        this.uniforms = new SFOGLUniform[uniforms.length];
        for (int i = 0; i < uniforms.length; i++) {
            this.uniforms[i] = new SFOGLUniform(uniforms[i]);
        }
    }

    public void init() {
        if (!initialized) {
            compileShader();
            compileData();
        }
    }

    public void compileData() {
        for (int i = 0; i < attribs.length; i++) {
            attribs[i].init(shadingProgram);
        }
        for (int i = 0; i < uniforms.length; i++) {
            uniforms[i].init(shadingProgram);
        }
    }

    public int getAttribLocation(int index) {
        return attribs[index].getAttribLocation();
    }

    public void bindAttributef(int index, int vertexBufferObject, int valueSize) {
        attribs[index].bindAttributef(vertexBufferObject, valueSize);
    }

    public int getUniformLocation(int index) {
        return uniforms[index].getUniformLocation();
    }

    public float[] getUniformValue(int index) {
        return uniforms[index].getValue();
    }

    public void setUniformValue(int index, float... value) {
        uniforms[index].setValue(value);
    }

    public void setUniformValuei(int index, int... value) {
        uniforms[index].setValuei(value);
    }


    public SFOGLShader(String vertexShader, String fragmentShader) {
        super();
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public void setShaders(String fragmentShader, String vertexShader) {
        this.fragmentShader = fragmentShader;
        this.vertexShader = vertexShader;
    }

    public void compileShaderWithInfos(PrintStream writer_) {

        this.shadingProgram = GLES20.glCreateProgram();

        Log.i("SFOGLShader", "Compiling Vertex Shader");
        Log.i("SFOGLShader", vertexShader);
        compileVertexShader();
        String compilerOutput = compiledShaderInfo(vShader);
        //writer.println(compilerOutput);
        Log.i("SFOGLShader", compilerOutput);

        Log.i("SFOGLShader", "Compiling Fragment Shader");
        Log.i("SFOGLShader", fragmentShader);
        compileFragmentShader();
        compilerOutput = compiledShaderInfo(fShader);
        //writer.println(compilerOutput);
        Log.i("SFOGLShader", compilerOutput);

        Log.i("SFOGLShader", "Linking Program");
        compileProgram(shadingProgram, vShader, fShader);
        compilerOutput = compiledProgramInfo(shadingProgram);
        //writer.println(compilerOutput);
        Log.i("SFOGLShader", compilerOutput);
    }

    public void compileShader() {

        this.shadingProgram = GLES20.glCreateProgram();

        compileVertexShader();

        compileFragmentShader();

        compileProgram(shadingProgram, vShader, fShader);

        initialized = true;
    }

    public void compileFragmentShader() {
        this.fShader = loadShader(fragmentShader, GLES20.GL_FRAGMENT_SHADER);
    }

    public void compileVertexShader() {
        this.vShader = loadShader(vertexShader, GLES20.GL_VERTEX_SHADER);
    }

    public void apply() {
        GLES20.glUseProgram(shadingProgram);
    }

    public void unapply(GLES20 gl) {
        GLES20.glUseProgram(0);
    }

    public int getShadingProgram() {
        return shadingProgram;
    }

    public void delete(GLES20 gl) {
        GLES20.glDeleteProgram(shadingProgram);
        GLES20.glDeleteShader(vShader);
        GLES20.glDeleteShader(fShader);
    }

    public static void compileProgram(int shadingProgram, int vShader, int fShader) {
        GLES20.glAttachShader(shadingProgram, vShader);

        GLES20.glAttachShader(shadingProgram, fShader);

        GLES20.glLinkProgram(shadingProgram);

        GLES20.glValidateProgram(shadingProgram);
    }

    public static String compiledShaderInfo(int shader) {
        int status[] = new int[1];
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            String info = GLES20.glGetShaderInfoLog(shader);
            GLES20.glDeleteShader(shader);
            throw new RuntimeException("Could not compile shader " + ":" + info);
        }

        //TODO : this is boring
        /*GLES20.glGetObjectParameterivARB(shader,GLES20.GL_OBJECT_COMPILE_STATUS_ARB,status,0);

		if(status[0]==0){
			int len[]=new int[1];
			GLES20.glGetObjectParameterivARB(shader, GLES20.GL_OBJECT_COMPILE_STATUS_ARB,
					len,0);

			byte[] b=new byte[20000];
			GLES20.glGetInfoLogARB(shader,b.length,status,0,b,0);

			return new String(b);
		}*/

        return "";
    }

    public static String compiledProgramInfo(int program) {

        int[] status = new int[1];
        /* TODO : this is boring :(
        GLES20.glGetObjectParameterivARB(program, GLES20.GL_OBJECT_LINK_STATUS_ARB, status,0);
		
		//System.out.println("Status "+status2[0]);
		if(status[0]==0){
			int len[]=new int[1];
			GLES20.glGetObjectParameterivARB(program, GL2.GL_OBJECT_COMPILE_STATUS_ARB,
					len,0);

			byte[] b=new byte[2000];
			GLES20.glGetInfoLogARB(program,b.length,status,0,b,0);

			return new String(b);
		}*/

        return "";

    }

    public static void bindAttribs(GLES20 gl, int program, String... attribs) {
        for (int i = 0; i < attribs.length; i++) {
            GLES20.glBindAttribLocation(program, i, attribs[i]);
        }
    }

    public static int loadShader(String shaderSource, int shaderType) {
        int shaderID = GLES20.glCreateShader(shaderType);
        GLES20.glShaderSource(shaderID, shaderSource);
        GLES20.glCompileShader(shaderID);
        return shaderID;
    }

	/*public static int loadShader(String[] shaderSource, int shaderType) {
        int shaderID = GLES20.glCreateShader(shaderType);
		int[] shaderLength=new int[shaderSource.length];
		for (int i = 0; i < shaderLength.length; i++) {
			shaderLength[i]=shaderSource.length;
		}
		GLES20.glShaderSource(shaderID, shaderSource);
		GLES20.glCompileShader(shaderID);
		return shaderID;
	}*/
}

