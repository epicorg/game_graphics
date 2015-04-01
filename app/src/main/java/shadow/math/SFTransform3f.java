package shadow.math;

/**
 * A Transform in a 3D space
 *
 * @author Alessandro Martinelli
 */
public class SFTransform3f extends SFValue {

    private static float[] multTmpVal = new float[12];
    float[] v = new float[12];

    public SFTransform3f() {
        getV()[0] = 1;
        getV()[4] = 1;
        getV()[8] = 1;
    }

    @Override
    public int getSize() {
        return 12;
    }

    @Override
    public float[] getV() {
        return v;
    }

    public SFValue cloneValue() {
        SFTransform3f transform = new SFTransform3f();
        transform.set(this);
        return transform;
    }

    public void setPosition(float x, float y, float z) {
        getV()[9] = x;
        getV()[10] = y;
        getV()[11] = z;
    }

    public void setMatrix(float a, float b, float c, float d, float e, float f,
                          float g, float h, float i) {
        getV()[0] = a;
        getV()[1] = b;
        getV()[2] = c;
        getV()[3] = d;
        getV()[4] = e;
        getV()[5] = f;
        getV()[6] = g;
        getV()[7] = h;
        getV()[8] = i;
    }

    public void setMatrix(SFMatrix3f matrix) {
        for (int i = 0; i < matrix.getV().length; i++) {
            this.getV()[i] = matrix.getV()[i];
        }
    }

    public void setPosition(SFVertex3f position) {
        for (int i = 0; i < position.getV().length; i++) {
            this.getV()[i + 9] = position.getV()[i];
        }
    }

    public void getMatrix(SFMatrix3f matrix) {
        for (int i = 0; i < matrix.getV().length; i++) {
            matrix.getV()[i] = this.getV()[i];
        }
    }

    public void getPosition(SFVertex3f position) {
        for (int i = 0; i < position.getV().length; i++) {
            position.getV()[i] = this.getV()[i + 9];
        }
    }

    public void transform(SFValue position) {
        float x = position.getV()[0];
        float y = position.getV()[1];
        float z = position.getV()[2];
        position.getV()[0] = x * getV()[0] + y * getV()[1] + z * getV()[2] + getV()[9];
        position.getV()[1] = x * getV()[3] + y * getV()[4] + z * getV()[5] + getV()[10];
        position.getV()[2] = x * getV()[6] + y * getV()[7] + z * getV()[8] + getV()[11];
    }

    public void transformDir(SFValue dir) {
        float x = dir.getV()[0];
        float y = dir.getV()[1];
        float z = dir.getV()[2];
        dir.getV()[0] = x * getV()[0] + y * getV()[1] + z * getV()[2];
        dir.getV()[1] = x * getV()[3] + y * getV()[4] + z * getV()[5];
        dir.getV()[2] = x * getV()[6] + y * getV()[7] + z * getV()[8];
    }

    public synchronized void mult(SFTransform3f transform) {

        float[] val = transform.getV();

        multTmpVal[0] = getV()[0] * val[0] + getV()[1] * val[3] + getV()[2] * val[6];
        multTmpVal[1] = getV()[0] * val[1] + getV()[1] * val[4] + getV()[2] * val[7];
        multTmpVal[2] = getV()[0] * val[2] + getV()[1] * val[5] + getV()[2] * val[8];

        multTmpVal[3] = getV()[3] * val[0] + getV()[4] * val[3] + getV()[5] * val[6];
        multTmpVal[4] = getV()[3] * val[1] + getV()[4] * val[4] + getV()[5] * val[7];
        multTmpVal[5] = getV()[3] * val[2] + getV()[4] * val[5] + getV()[5] * val[8];

        multTmpVal[6] = getV()[6] * val[0] + getV()[7] * val[3] + getV()[8] * val[6];
        multTmpVal[7] = getV()[6] * val[1] + getV()[7] * val[4] + getV()[8] * val[7];
        multTmpVal[8] = getV()[6] * val[2] + getV()[7] * val[5] + getV()[8] * val[8];

        multTmpVal[9] = getV()[0] * val[9] + getV()[1] * val[10] + getV()[2] * val[11] + getV()[9];
        multTmpVal[10] = getV()[3] * val[9] + getV()[4] * val[10] + getV()[5] * val[11] + getV()[10];
        multTmpVal[11] = getV()[6] * val[9] + getV()[7] * val[10] + getV()[8] * val[11] + getV()[11];

        for (int i = 0; i < multTmpVal.length; i++) {
            this.getV()[i] = multTmpVal[i];
        }
    }

}
