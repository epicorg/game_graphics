package sfogl2;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class SFOGLBufferObject {

    public static final int SIZE_OF_FLOAT = 4;
    public static final int SIZE_OF_SHORT = 2;

    private int bufferObject;

    public SFOGLBufferObject() {

    }

    public int getBufferObject() {
        return bufferObject;
    }

    public void setBufferObject(int bufferObject) {
        this.bufferObject = bufferObject;
    }

    public void loadData(float[] data) {
        this.bufferObject = loadBufferObjectf(data);
    }

    public void loadData(short[] data) {
        this.bufferObject = loadBufferObjects(data);
    }

    public void drawAsIndexedBuffer(int primitiveType, int primitiveIndicesSize) {
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, bufferObject);
        GLES20.glDrawElements(primitiveType, primitiveIndicesSize, GLES20.GL_UNSIGNED_SHORT, 0);
    }

    public static ByteBuffer loadFloatBuffer(float[] data) {

        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length * SIZE_OF_FLOAT);
        buffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = buffer.asFloatBuffer();
        floatBuffer.put(data, 0, data.length);
        floatBuffer.rewind();
        return buffer;
    }

    public static ByteBuffer loadShortBuffer(short[] data) {

        ByteBuffer buffer = ByteBuffer.allocateDirect(data.length * SIZE_OF_SHORT);
        buffer.order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = buffer.asShortBuffer();
        shortBuffer.put(data, 0, data.length);
        shortBuffer.rewind();
        return buffer;
    }

    public static int loadBufferObjectf(float[] data) {

        int vbo = createBufferObject();

        ByteBuffer byteData = loadFloatBuffer(data);
        byteData.rewind();
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.length * SIZE_OF_FLOAT, byteData, GLES20.GL_STATIC_DRAW);

        return vbo;
    }

    public static int loadBufferObjects(short[] data) {

        int vbo = createBufferObject();

        ByteBuffer byteData = loadShortBuffer(data);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vbo);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, data.length * SIZE_OF_SHORT, byteData, GLES20.GL_STATIC_DRAW);

        return vbo;
    }

    private static int createBufferObject() {
        int[] vbos = new int[1];
        GLES20.glGenBuffers(1, vbos, 0);
        int vbo = vbos[0];
        return vbo;
    }

}
