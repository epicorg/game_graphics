package shadow.math;

/**
 * A matrix of 3x3 elements as a set of 3 horizontal vector (a,b,c) (d,e,f) (g,h,i)
 *
 * @author Alessandro Martinelli
 * @template Data_Structure
 */
public class SFMatrix3f extends SFValue {

    public SFMatrix3f() {
        getV()[0] = 1;
        getV()[4] = 1;
        getV()[8] = 1;
    }

    public SFMatrix3f(float[] values) {
        getV()[0] = 1;
        getV()[4] = 1;
        getV()[8] = 1;
    }

    public SFMatrix3f(float a, float b, float c, float d, float e, float f, float g, float h, float i) {
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

    float[] v = new float[9];

    @Override
    public int getSize() {
        return 9;
    }

    @Override
    public float[] getV() {
        return v;
    }

    public SFMatrix3f cloneValue() {
        return new SFMatrix3f(getV()[0], getV()[1], getV()[2], getV()[3], getV()[4], getV()[5], getV()[6], getV()[7], getV()[8]);
    }

    public static SFMatrix3f getRotationZ(float angle) {
        SFMatrix3f m = new SFMatrix3f();
        float cos = (float) (Math.cos(angle));
        float sin = (float) (Math.sin(angle));

        m.getV()[0] = cos;
        m.getV()[1] = sin;
        m.getV()[2] = 0;
        m.getV()[3] = -sin;
        m.getV()[4] = cos;
        m.getV()[5] = 0;
        m.getV()[6] = 0;
        m.getV()[7] = 0;
        m.getV()[8] = 1;

        return m;
    }

    public static SFMatrix3f getRotationY(float angle) {
        SFMatrix3f m = new SFMatrix3f();
        float cos = (float) (Math.cos(angle));
        float sin = (float) (Math.sin(angle));

        m.getV()[0] = cos;
        m.getV()[1] = 0;
        m.getV()[2] = sin;
        m.getV()[3] = 0;
        m.getV()[4] = 1;
        m.getV()[5] = 0;
        m.getV()[6] = -sin;
        m.getV()[7] = 0;
        m.getV()[8] = cos;

        return m;
    }

    public static SFMatrix3f getRotationX(float angle) {
        SFMatrix3f m = new SFMatrix3f();
        float cos = (float) (Math.cos(angle));
        float sin = (float) (Math.sin(angle));

        m.getV()[0] = 1;
        m.getV()[1] = 0;
        m.getV()[2] = 0;
        m.getV()[3] = 0;
        m.getV()[4] = cos;
        m.getV()[5] = sin;
        m.getV()[6] = 0;
        m.getV()[7] = -sin;
        m.getV()[8] = cos;

        return m;
    }

    public static SFMatrix3f getTransposed(SFMatrix3f m) {
        SFMatrix3f n = new SFMatrix3f();

        n.getV()[0] = m.getV()[0];
        n.getV()[3] = m.getV()[1];
        n.getV()[6] = m.getV()[2];
        n.getV()[1] = m.getV()[3];
        n.getV()[4] = m.getV()[4];
        n.getV()[7] = m.getV()[5];
        n.getV()[2] = m.getV()[6];
        n.getV()[5] = m.getV()[7];
        n.getV()[8] = m.getV()[8];

        return n;
    }


    public static SFMatrix3f getIdentity() {
        SFMatrix3f n = new SFMatrix3f();

        n.getV()[0] = 1;
        n.getV()[3] = 0;
        n.getV()[6] = 0;
        n.getV()[1] = 0;
        n.getV()[4] = 1;
        n.getV()[7] = 0;
        n.getV()[2] = 0;
        n.getV()[5] = 0;
        n.getV()[8] = 1;

        return n;
    }

    public static SFMatrix3f getScale(float scaleX, float scaleY, float scaleZ) {
        SFMatrix3f n = new SFMatrix3f();

        n.getV()[0] = scaleX;
        n.getV()[3] = 0;
        n.getV()[6] = 0;
        n.getV()[1] = 0;
        n.getV()[4] = scaleY;
        n.getV()[7] = 0;
        n.getV()[2] = 0;
        n.getV()[5] = 0;
        n.getV()[8] = scaleZ;

        return n;
    }

    public static SFMatrix3f getInverse(SFMatrix3f m) {
        SFMatrix3f n = new SFMatrix3f();

        float delta = m.getV()[0] * (m.getV()[4] * m.getV()[8] - m.getV()[5] * m.getV()[7]) - m.getV()[1] * (m.getV()[3] * m.getV()[8] - m.getV()[5] * m.getV()[6]) + m.getV()[2] * (m.getV()[3] * m.getV()[7] - m.getV()[4] * m.getV()[6]);

        if (delta != 0) {
            delta = 1 / delta;

            n.getV()[0] = delta * (m.getV()[4] * m.getV()[8] - m.getV()[5] * m.getV()[7]);
            n.getV()[1] = -delta * (m.getV()[1] * m.getV()[8] - m.getV()[2] * m.getV()[7]);
            n.getV()[2] = delta * (m.getV()[1] * m.getV()[5] - m.getV()[2] * m.getV()[4]);
            n.getV()[3] = -delta * (m.getV()[3] * m.getV()[8] - m.getV()[5] * m.getV()[6]);
            n.getV()[4] = delta * (m.getV()[0] * m.getV()[8] - m.getV()[2] * m.getV()[6]);
            n.getV()[5] = -delta * (m.getV()[0] * m.getV()[5] - m.getV()[2] * m.getV()[3]);
            n.getV()[6] = delta * (m.getV()[3] * m.getV()[7] - m.getV()[4] * m.getV()[6]);
            n.getV()[7] = -delta * (m.getV()[0] * m.getV()[7] - m.getV()[1] * m.getV()[6]);
            n.getV()[8] = delta * (m.getV()[0] * m.getV()[4] - m.getV()[1] * m.getV()[3]);
        }

        return n;
    }

//	public static SFMatrix3f getRotationMatrix(SFVertex4f q){
//		SFMatrix3f m=new SFMatrix3f();
//		
//		m.setA(1 - 2*(q.getY()*q.getY() + q.getZ()*q.getZ()));  
//		m.setB(-2*q.getZ()*q.getW()+2*q.getX()*q.getY());
//		m.setC(2*q.getW()*q.getY() +2*q.getX()*q.getZ());
//
//		m.setD(2*q.getZ()*q.getW()+2*q.getX()*q.getY());
//		m.setE(1 - 2*(q.getX()*q.getX() + q.getZ()*q.getZ()));
//		m.setF(2*q.getY()*q.getZ()-2*q.getX()*q.getW());
//
//		m.setG(2*q.getX()*q.getZ()-2*q.getW()*q.getY());
//		m.setH(2*q.getY()*q.getY()-2*q.getW()*q.getX());
//		m.setI(1 - 2*(q.getX()*q.getX() + q.getY()*q.getY()));
//		
//		return m;
//	}

    public String toString() {
        return "SFMatrix3f \n " + getV()[0] + " " + getV()[1] + " " + getV()[2] + " \n" +
                getV()[3] + " " + getV()[4] + " " + getV()[5] + " \n" +
                getV()[6] + " " + getV()[7] + " " + getV()[8] + " \n";
    }


    public SFMatrix3f MultMatrix(SFMatrix3f m) {
        SFMatrix3f n = new SFMatrix3f();

        n.getV()[0] = getV()[0] * m.getV()[0] + getV()[1] * m.getV()[3] + getV()[2] * m.getV()[6];
        n.getV()[1] = getV()[0] * m.getV()[1] + getV()[1] * m.getV()[4] + getV()[2] * m.getV()[7];
        n.getV()[2] = getV()[0] * m.getV()[2] + getV()[1] * m.getV()[5] + getV()[2] * m.getV()[8];

        n.getV()[3] = getV()[3] * m.getV()[0] + getV()[4] * m.getV()[3] + getV()[5] * m.getV()[6];
        n.getV()[4] = getV()[3] * m.getV()[1] + getV()[4] * m.getV()[4] + getV()[5] * m.getV()[7];
        n.getV()[5] = getV()[3] * m.getV()[2] + getV()[4] * m.getV()[5] + getV()[5] * m.getV()[8];

        n.getV()[6] = getV()[6] * m.getV()[0] + getV()[7] * m.getV()[3] + getV()[8] * m.getV()[6];
        n.getV()[7] = getV()[6] * m.getV()[1] + getV()[7] * m.getV()[4] + getV()[8] * m.getV()[7];
        n.getV()[8] = getV()[6] * m.getV()[2] + getV()[7] * m.getV()[5] + getV()[8] * m.getV()[8];

        return n;
    }

    public SFVertex3f Mult(SFVertex3f p) {
        SFVertex3f n = new SFVertex3f();

        n.setX(getV()[0] * p.getX() + getV()[1] * p.getY() + getV()[2] * p.getZ());
        n.setY(getV()[3] * p.getX() + getV()[4] * p.getY() + getV()[5] * p.getZ());
        n.setZ(getV()[6] * p.getX() + getV()[7] * p.getY() + getV()[8] * p.getZ());

        return n;
    }

    public void transform(SFVertex3f p) {

        float x = (float) p.getX(), y = (float) p.getY(), z = (float) p.getZ();

        p.setX(getV()[0] * x + getV()[1] * y + getV()[2] * z);
        p.setY(getV()[3] * x + getV()[4] * y + getV()[5] * z);
        p.setZ(getV()[6] * x + getV()[7] * y + getV()[8] * z);

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

    /**
     * @return Returns the e.
     */
    public float getE() {
        return getV()[4];
    }

    /**
     * @param e The e to set.
     */
    public void setE(float e) {
        getV()[4] = e;
    }

    /**
     * @return Returns the f.
     */
    public float getF() {
        return getV()[5];
    }

    /**
     * @param f The f to set.
     */
    public void setF(float f) {
        getV()[5] = f;
    }

    /**
     * @return Returns the g.
     */
    public float getG() {
        return getV()[6];
    }

    /**
     * @param g The g to set.
     */
    public void setG(float g) {
        getV()[6] = g;
    }

    /**
     * @return Returns the h.
     */
    public float getH() {
        return getV()[7];
    }

    /**
     * @param h The h to set.
     */
    public void setH(float h) {
        getV()[7] = h;
    }

    /**
     * @return Returns the i.
     */
    public float getI() {
        return getV()[8];
    }

    /**
     * @param i The i to set.
     */
    public void setI(float i) {
        getV()[8] = i;
    }


}