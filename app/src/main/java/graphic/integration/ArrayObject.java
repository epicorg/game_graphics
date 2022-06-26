package graphic.integration;

public class ArrayObject {

    private final float[] verticesBuffer;
    private final float[] normalsBuffer;
    private final float[] txCoordinatesBuffer;
    private final short[] indicesBuffer;

    public ArrayObject(float[] verticesBuffer, float[] normalsBuffer,
                       float[] txCoordinatesBuffer, short[] indicesBuffer) {
        super();
        this.verticesBuffer = verticesBuffer;
        this.normalsBuffer = normalsBuffer;
        this.txCoordinatesBuffer = txCoordinatesBuffer;
        this.indicesBuffer = indicesBuffer;
    }

    public float[] getNormalsBuffer() {
        return normalsBuffer;
    }

    public float[] getTxCoordinatesBuffer() {
        return txCoordinatesBuffer;
    }

    public float[] getVerticesBuffer() {
        return verticesBuffer;
    }

    public boolean isTxCoordinate() {
        return txCoordinatesBuffer.length > 0;
    }

    public short[] getIndicesBuffer() {
        return indicesBuffer;
    }

}
