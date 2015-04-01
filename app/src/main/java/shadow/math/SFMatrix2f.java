package shadow.math;

/**
 * A matrix of 2x2 elements as a set of 2 horizontal vector (a,b) (d,e)
 *
 * @author Alessandro Martinelli
 */
public class SFMatrix2f extends SFValue {

    float[] v = new float[4];

    public SFMatrix2f() {
    }

    public SFMatrix2f(float a, float b, float c, float d) {
        getV()[0] = a;
        getV()[1] = b;
        getV()[2] = c;
        getV()[3] = d;
    }

    public static SFMatrix2f getRotationZ(float angle) {
        SFMatrix2f m = new SFMatrix2f();
        float cos = (float) (Math.cos(angle));
        float sin = (float) (Math.sin(angle));

        m.getV()[0] = cos;
        m.getV()[1] = sin;
        m.getV()[2] = -sin;
        m.getV()[3] = cos;

        return m;
    }

    public static SFMatrix2f getTransposed(SFMatrix2f m) {
        SFMatrix2f n = new SFMatrix2f();

        n.getV()[0] = m.getV()[0];
        n.getV()[2] = m.getV()[1];
        n.getV()[1] = m.getV()[2];
        n.getV()[3] = m.getV()[3];

        return n;
    }

    public static SFMatrix2f getIdentity() {
        SFMatrix2f n = new SFMatrix2f();

        n.getV()[0] = 1;
        n.getV()[2] = 0;
        n.getV()[1] = 0;
        n.getV()[3] = 1;

        return n;
    }

    public static SFMatrix2f getScale(float scaleX, float scaleY) {
        SFMatrix2f n = new SFMatrix2f();

        n.getV()[0] = scaleX;
        n.getV()[2] = 0;
        n.getV()[1] = 0;
        n.getV()[3] = scaleY;

        return n;
    }

    public static SFMatrix2f getInverse(SFMatrix2f m) {
        SFMatrix2f n = new SFMatrix2f();

        float delta = m.getV()[0] * m.getV()[3] - m.getV()[2] * m.getV()[1];

        if (delta != 0) {
            delta = 1 / delta;

            n.getV()[0] = delta * (m.getV()[3]);
            n.getV()[1] = -delta * (m.getV()[1]);
            n.getV()[2] = delta * (-m.getV()[2]);
            n.getV()[3] = delta * (m.getV()[0]);
        }

        return n;
    }

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public float[] getV() {
        return v;
    }

    public SFMatrix2f cloneValue() {
        return new SFMatrix2f(getV()[0], getV()[1], getV()[2], getV()[3]);
    }

    public void set(SFMatrix2f m) {
        getV()[0] = m.getV()[0];
        getV()[1] = m.getV()[1];
        getV()[2] = m.getV()[2];
        getV()[3] = m.getV()[3];
    }

    public String toString() {
        return "SFMatrix2f \n " + getV()[0] + " " + getV()[1] + " " + getV()[2] + " \n" + getV()[2] + " " + getV()[3]
                + " \n";
    }

    public SFMatrix2f MultMatrix(SFMatrix2f m) {
        SFMatrix2f n = new SFMatrix2f();

        n.getV()[0] = getV()[0] * m.getV()[0] + getV()[1] * m.getV()[2];
        n.getV()[1] = getV()[0] * m.getV()[1] + getV()[1] * m.getV()[3];
        n.getV()[2] = getV()[2] * m.getV()[0] + getV()[3] * m.getV()[2];
        n.getV()[3] = getV()[2] * m.getV()[1] + getV()[3] * m.getV()[3];

        return n;
    }

    public SFVertex2f Mult(SFVertex2f p) {
        SFVertex2f n = new SFVertex2f();

        n.setX(getV()[0] * p.getX() + getV()[1] * p.getY());
        n.setY(getV()[2] * p.getX() + getV()[2] * p.getY());

        return n;
    }

    public void transform(SFVertex2f p) {

        float x = p.getX(), y = p.getY();

        p.setX(getV()[0] * x + getV()[1] * y);
        p.setY(getV()[2] * x + getV()[2] * y);
    }

    /**
     * @return Returns the a.
     */
    public float getA() {
        return getV()[0];
    }

    /**
     * @param a The a to set.
     */
    public void setA(float a) {
        getV()[0] = a;
    }

    /**
     * @return Returns the b.
     */
    public float getB() {
        return getV()[1];
    }

    /**
     * @param b The b to set.
     */
    public void setB(float b) {
        getV()[1] = b;
    }

    /**
     * @return Returns the c.
     */
    public float getC() {
        return getV()[2];
    }

    /**
     * @param c The c to set.
     */
    public void setC(float c) {
        getV()[2] = c;
    }

    /**
     * @return Returns the d.
     */
    public float getD() {
        return getV()[3];
    }

    /**
     * @param d The d to set.
     */
    public void setD(float d) {
        getV()[3] = d;
    }


}