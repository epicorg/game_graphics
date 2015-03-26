package shadow.math;

/**
 * @author Alessandro Martinelli
 * @template Data_Structure
 */
public class SFVertex4f extends SFValue {

    float[] v = new float[4];

    /**
     * Create a new 4f value, assigning it (0,0,0,0)
     */
    public SFVertex4f() {
        this(0, 0, 0, 0);
    }

    public SFVertex4f(SFVertex3f v) {
        this(0, 0, 0, 0);
        set(v);
    }

    /**
     * Create a new 4f value, assigning it
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public SFVertex4f(float x, float y, float z, float w) {
        this.set4f(x, y, z, w);
    }

    /**
     * Generate the middle Point between two poits A and B
     *
     * @param A the first Point
     * @param B the second Point
     * @return the middle Point
     * @throws ArrayIndexOutOfBoundsException
     */
    public static SFVertex4f middle(SFVertex4f A, SFVertex4f B) {
        return new SFVertex4f(
                (A.getV()[0] + B.getV()[0]) * 0.5f, (A.getV()[1] + B.getV()[1]) * 0.5f, (A.getV()[2] + B.getV()[2]) * 0.5f, (A.getV()[3] + B.getV()[3]) * 0.5f
        );
    }

    public static SFVertex4f linearVertex(float t1, SFVertex4f v1, SFVertex4f v2) {
        SFVertex4f v = new SFVertex4f();
        v.addMult4f(1 - t1, v1);
        v.addMult4f(t1, v2);
        return v;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public float[] getV() {
        return v;
    }

    /**
     * Specific 4f version of the add method.
     *
     * @param q
     */
    public void add4f(SFVertex4f q) {
        getV()[0] += q.getV()[0];
        getV()[1] += q.getV()[1];
        getV()[2] += q.getV()[2];
        getV()[3] += q.getV()[3];
    }

    /**
     * Specific 4f version of the addMult method.
     *
     * @param a
     * @param vx
     */
    public void addMult4f(float a, SFVertex4f vx) {
        getV()[0] += vx.getV()[0] * a;
        getV()[1] += vx.getV()[1] * a;
        getV()[2] += vx.getV()[2] * a;
        getV()[3] += vx.getV()[3] * a;
    }

    public SFVertex4f cloneValue() {
        return new SFVertex4f(getV()[0], getV()[1], getV()[2], getV()[3]);
    }

    /**
     * Specific 4f version of the dot method.
     *
     * @param vx
     * @return
     */
    public double dot4f(SFVertex4f vx) {
        return vx.getV()[0] * getV()[0] + vx.getV()[1] * getV()[1] + vx.getV()[2] * getV()[2] + vx.getV()[3] * getV()[3];
    }

    /**
     * @return the w value
     */
    public float getW() {
        return getV()[3];
    }

    public void setW(float w) {
        getV()[3] = w;
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
        return this.getV()[1];
    }

    public void setY(float y) {
        this.getV()[1] = y;
    }

    /**
     * @return the z value
     */
    public float getZ() {
        return this.getV()[2];
    }

    public void setZ(float z) {
        this.getV()[2] = z;
    }

    /**
     * Specific 4f version of the mult method.
     *
     * @param a
     */
    public void mult4f(float a) {
        getV()[0] *= a;
        getV()[1] *= a;
        getV()[2] *= a;
        getV()[3] *= a;
    }

    /**
     * Set this vector-vertex to be a unit vector with the same direction
     */
    public void normalize4f() {
        float lengthRec = 1 / (float) (Math.sqrt(getV()[0] * getV()[0] + getV()[1] * getV()[1] + getV()[2] * getV()[2] + getV()[3] * getV()[3]));
        getV()[0] *= lengthRec;
        getV()[1] *= lengthRec;
        getV()[2] *= lengthRec;
        getV()[3] *= lengthRec;
    }

    /**
     * Scale this vector
     * This.x=This.x*sx
     * This.y=This.y*sy
     * This.z=This.z*sz
     * This.w=This.w*sw
     *
     * @param sx
     * @param sy
     * @param sz
     * @param sw
     */
    public void scale4f(float sx, float sy, float sz, float sw) {
        this.getV()[0] *= sx;
        this.getV()[1] *= sy;
        this.getV()[2] *= sz;
        this.getV()[3] *= sw;
    }

    /**
     * Set the elements (x,y,z,w)
     */
    public void set4f(float x, float y, float z, float w) {
        this.getV()[0] = x;
        this.getV()[1] = y;
        this.getV()[2] = z;
        this.getV()[3] = w;
    }

    public void normalize3f() {
        float recLength = 1.0f / (float) (Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]));
        v[0] *= recLength;
        v[1] *= recLength;
        v[2] *= recLength;
    }

    /**
     * Specific 4f version of the subtract method.
     *
     * @param q
     */
    public void subtract4f(SFVertex4f q) {
        getV()[0] -= q.getV()[0];
        getV()[1] -= q.getV()[1];
        getV()[2] -= q.getV()[2];
        getV()[3] -= q.getV()[3];
    }
}
