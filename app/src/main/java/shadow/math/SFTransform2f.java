package shadow.math;

/**
 * A Transform in a 2D space
 *
 * @author Alessandro Martinelli
 */
public class SFTransform2f extends SFValue {

    float[] v = new float[6];

    public SFTransform2f() {
    }

    @Override
    public int getSize() {
        return 6;
    }

    @Override
    public float[] getV() {
        return v;
    }

    /**
     * @return
     * @deprecated
     */
    public SFTransform2f cloneValue() {
        SFTransform2f transform = new SFTransform2f();
        transform.set(this);
        return transform;
    }

    public void setMatrix(SFMatrix2f matrix) {
        for (int i = 0; i < matrix.getV().length; i++) {
            this.getV()[i] = matrix.getV()[i];
        }
    }

    public void setPosition(SFVertex2f position) {
        for (int i = 0; i < position.getV().length; i++) {
            this.getV()[i + 4] = position.getV()[i];
        }
    }

    public void getMatrix(SFMatrix2f matrix) {
        for (int i = 0; i < matrix.getV().length; i++) {
            matrix.getV()[i] = this.getV()[i];
        }
    }

    public void getPosition(SFVertex2f position) {
        for (int i = 0; i < position.getV().length; i++) {
            position.getV()[i] = this.getV()[i + 4];
        }
    }

}
