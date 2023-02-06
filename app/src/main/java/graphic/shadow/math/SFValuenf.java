package graphic.shadow.math;

/**
 * A N-sized vector.
 * <p/>
 * This is the base class for most of the ShadowFramework Data Structures and
 * contains the general implementation of array manipulation methods.
 *
 * @author Alessandro Martinelli
 * @template Data_Structure
 */
public class SFValuenf extends SFValue {

    private float[] v;

    /**
     * Generate a new SFValuenf, the internal value is set to null
     */
    public SFValuenf() {
        super();
    }

    /**
     * Generate a new SFValuenf given its size
     *
     * @param n
     */
    public SFValuenf(int n) {
        super();
        this.v = (new float[n]);
    }

    /**
     * Generate a new SFValuenf given its vector
     *
     * @param v vector
     */
    public SFValuenf(float[] v) {
        super();
        this.v = (v);
    }

    /**
     * Generate the middle Point between two points A and B
     *
     * @param A the first Point
     * @param B the second Point
     * @return the middle Point
     */
    public static SFValuenf middle(SFValuenf A, SFValuenf B)
            throws ArrayIndexOutOfBoundsException {
        SFValuenf value = new SFValuenf(A.getV().length);
        for (int i = 0; i < A.getV().length; i++) {
            value.getV()[i] = (A.getV()[i] + B.getV()[i]) * 0.5f;
        }
        return value;
    }

    /**
     * Generate a clone of this Value
     *
     * @deprecated please, create a new vertex than call set or set2f
     */
    public SFValuenf cloneValue() {
        SFValuenf v = new SFValuenf(this.getV().length);
        v.set(this);
        return v;
    }

    @Override
    public int getSize() {
        return v.length;
    }

    @Override
    public float[] getV() {
        return v;
    }

}
