package sfogl.integration;

public class ArrayObject {

    private float[] verticesBuffer;
    private float[] normalsBuffer;
    private float[] txCoordsBuffer;
    private short[] indicesBuffer;
    
	public ArrayObject(float[] verticesBuffer, float[] normalsBuffer,
			float[] txCoordsBuffer, short[] indicesBuffer) {
		super();
		this.verticesBuffer = verticesBuffer;
		this.normalsBuffer = normalsBuffer;
		this.txCoordsBuffer = txCoordsBuffer;
		this.indicesBuffer = indicesBuffer;
	}

    public float[] getNormalsBuffer() {
		return normalsBuffer;
	}
    
    public float[] getTxCoordsBuffer() {
		return txCoordsBuffer;
	}
    
    public float[] getVerticesBuffer() {
		return verticesBuffer;
	}
    
    public boolean isTxCoord(){
    	return txCoordsBuffer.length>0;
    }

    public short[] getIndicesBuffer() {
        return indicesBuffer;
    }

}
