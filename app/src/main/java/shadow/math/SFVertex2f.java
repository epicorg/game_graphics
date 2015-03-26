package shadow.math;

/**
 * A value of 2 elements. A vector or a vertex (x,y). Add alternative, specific, more fast
 * methods 2f to the base nf method
 *
 * @author Alessandro
 * @template Data_Structure
 */
public class SFVertex2f extends SFValue {

    float[] v = new float[2];


    /**
     * Create a new 2f value, assigning it (0,0)
     */
    public SFVertex2f() {
        this(0, 0);
    }

    /**
     * Create a new 2f value, assigning it
     *
     * @param x
     * @param y
     */
    public SFVertex2f(double x, double y) {
        //super(2);
        getV()[0] = (float) x;
        getV()[1] = (float) y;
    }

    /**
     * Create a new 2f value, assigning it
     *
     * @param x
     * @param y
     */
    public SFVertex2f(float x, float y) {
        //super(2);
        getV()[0] = x;
        getV()[1] = y;
    }

    /**
     * @param vertex
     * @deprecated please, create a new vertex than call set or set2f
     */
    public SFVertex2f(SFVertex2f vertex) {
        //super(2);
        set(vertex);
    }

    /**
     * Return the distance between two vertices
     *
     * @param v1
     * @param v2
     * @return
     */
    public static float getDistance(SFVertex2f v1, SFVertex2f v2) {
        float x = v1.getX() - v2.getX();
        float y = v1.getY() - v2.getY();
        return (float) (Math.sqrt(x * x + y * y));
    }

    /**
     * Generate the middle Point between two poits A and B
     *
     * @param A the first Point
     * @param B the second Point
     * @return the middle Point
     * @throws ArrayIndexOutOfBoundsException
     */
    public static SFVertex2f middle(SFVertex2f A, SFVertex2f B) {
        return new SFVertex2f((A.getV()[0] + B.getV()[0]) * 0.5f,
                (A.getV()[1] + B.getV()[1]) * 0.5f);
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public float[] getV() {
        return v;
    }

    /**
     * Specific 2f version of the add method.
     *
     * @param vx the constant to be added
     */
    public void add2f(SFVertex2f vx) {
        getV()[0] += vx.getV()[0];
        getV()[1] += vx.getV()[1];
    }

    /**
     * Specific 2f version of the addMult method.
     *
     * @param a
     * @param vx
     */
    public void addMult2f(float a, SFVertex2f vx) {
        getV()[0] += vx.getV()[0] * a;
        getV()[1] += vx.getV()[1] * a;
    }

    /**
     * @deprecated please, create a new vertex than call set or set2f
     */
    public SFVertex2f cloneValue() {
        return new SFVertex2f(getV()[0], getV()[1]);
    }

    /**
     * Specific 2f version of the dot method.
     *
     * @param vx
     * @return
     */
    public float dot2f(SFVertex2f vx) {
        return vx.getV()[0] * getV()[0] + vx.getV()[1] * getV()[1];
    }

    /**
     * @return the length of the Vector
     */
    public float getLength() {
        return (float) (Math.sqrt(getV()[0] * getV()[0] + getV()[1] * getV()[1]));
    }

    /**
     * @return the x value
     */
    public float getX() {
        return this.getV()[0];
    }

    public void setX(float x) {
        this.getV()[0] = x;
    }

    /**
     * @return the y value
     */
    public float getY() {
        return getV()[1];
    }

    public void setY(float y) {
        getV()[1] = y;
    }

    /**
     * Specific 2f version of the mult method.
     *
     * @param m
     */
    public void mult2f(float m) {
        getV()[0] *= m;
        getV()[1] *= m;
    }

    /**
     * Set this vector-vertex to be a unit vector with the same direction
     */
    public void normalize2f() {
        float lengthRec = 1 / getLength();
        getV()[0] *= lengthRec;
        getV()[1] *= lengthRec;
    }

    /**
     * Scale this vector
     * This.x=This.x*sx
     * This.y=This.y*sy
     *
     * @param sx
     * @param sy
     */
    public void scale2f(float sx, float sy) {
        getV()[0] *= sx;
        getV()[1] *= sy;
    }

    /**
     * Set both the x and the y
     *
     * @param x
     * @param y
     */
    public void set2f(float x, float y) {
        this.getV()[0] = x;
        this.getV()[1] = y;
    }

    /**
     * Specific 2f version of the subtract method.
     *
     * @param vx
     */
    public void subtract2f(SFVertex2f vx) {
        getV()[0] -= vx.getV()[0];
        getV()[1] -= vx.getV()[1];
    }

}
