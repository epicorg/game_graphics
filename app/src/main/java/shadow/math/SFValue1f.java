package shadow.math;

/**
 * A value of 1 element. A vector (x). Add alternative, specific, more fast
 * methods 1f to the base nf method
 *
 * @author Alessandro
 * @template Data_Structure
 */
public class SFValue1f extends SFValue {

    float[] v = new float[1];

    /**
     * Create a new 1f value, assigning it
     *
     * @param x
     */
    public SFValue1f(double x) {
        this.getV()[0] = (float) x;
    }

    /**
     * Create a new 1f value, assigning it
     *
     * @param x
     */
    public SFValue1f(float x) {
        this.getV()[0] = x;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public float[] getV() {
        return v;
    }

    /**
     * Add a constant to this 1f value
     *
     * @param m the constant to be added
     */
    public void add1f(float dX) {
        getV()[0] += dX;
    }

    /**
     * Clone this value
     */
    public SFValue1f cloneValue() {
        return new SFValue1f(this.getV()[0]);
    }

    /**
     * get the 1f content value
     *
     * @return
     */
    public float getX() {
        return getV()[0];
    }

    /**
     * get the 1f content value
     *
     * @return
     */
    public void setX(float x) {
        getV()[0] = x;
    }

    /**
     * Mult this 1f value for a constant
     *
     * @param m the constant to be multiplied
     */
    public void mult1f(float m) {
        getV()[0] *= m;
    }

    /**
     * Subtract a constant to this 1f value
     *
     * @param m the constant to be subtracted
     */
    public void subtract1f(float dX) {
        getV()[0] -= dX;
    }

}
