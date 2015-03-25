package shadow.math;

/**
 * A quaternion
 *
 * @author Alessandro Martinelli
 * @pattern Data_Extension
 */
public class SFQuaternion extends SFVertex4f {

    public SFQuaternion() {

    }

    public SFQuaternion(SFVertex3f direction, double angle) {
        float cos = (float) (Math.cos(angle * 0.5f));
        float sin = (float) (Math.sin(angle * 0.5f));
        getV()[0] = sin * direction.getX();
        getV()[1] = sin * direction.getY();
        getV()[2] = sin * direction.getZ();
        getV()[3] = cos;
    }

    /**
     * Quaternion multiplication. This = This * q;
     *
     * @param q
     */
    public void multTo(SFQuaternion q) {
        float w1 = getV()[3] * q.getV()[3] - getV()[0] * q.getV()[0] - getV()[1] * q.getV()[1] - getV()[2] * q.getV()[2];
        float x1 = getV()[3] * q.getV()[0] + getV()[0] * q.getV()[3] + getV()[2] * q.getV()[1] - getV()[1] * q.getV()[2];
        float y1 = getV()[3] * q.getV()[1] + getV()[1] * q.getV()[3] + getV()[0] * q.getV()[2] - getV()[2] * q.getV()[0];
        float z1 = getV()[3] * q.getV()[2] + getV()[2] * q.getV()[3] + getV()[1] * q.getV()[0] - getV()[0] * q.getV()[1];
        this.getV()[0] = x1;
        this.getV()[1] = y1;
        this.getV()[2] = z1;
        this.getV()[3] = w1;
    }


    public void setMatrix(SFMatrix3f matrix) {

        //from GamePhisics page 537
        double Rxx = matrix.getA();
        double Ryy = matrix.getE();
        double Rzz = matrix.getI();
        float[] v = matrix.getV();

        double Tr = Rxx + Ryy + Rzz;

        if (Tr < 0) {
            System.err.println("Case A");
            int i = 0, j = 1, k = 2;
            if (Ryy > Rxx) {
                i = 1;
                j = 2;
                k = 0;
            }
            if (Rzz > v[3 * i + i]) {
                i = 2;
                j = 0;
                k = 1;
            }
            double r = Math.sqrt(v[3 * i + i] - v[3 * j + j] - v[3 * k + k] + 1);
            double s = (0.5 / r);
            getV()[0] = (float) (0.5 * r);
            getV()[1] = (float) (s * (v[3 * i + j] + v[3 * j + i]));
            getV()[2] = (float) (s * (v[3 * i + k] + v[3 * k + i]));
            getV()[3] = (float) (s * (v[3 * k + j] - v[3 * j + k]));
        } else {
            double r = Math.sqrt(Tr + 1);
            double s = (0.5 / r);
            getV()[0] = (float) (s * (v[3 * 2 + 1] - v[3 * 1 + 2]));
            getV()[1] = (float) (s * (v[3 * 0 + 2] - v[3 * 2 + 0]));
            getV()[2] = (float) (s * (v[3 * 1 + 0] - v[3 * 0 + 1]));
            getV()[3] = (float) (0.5 * r);
        }
    }

    /**
     * Apply this quaternion as a rotation to a Vertex3f
     *
     * @param q
     */
    public SFVertex3f applyRotation(SFVertex3f a) {
        SFVertex3f b = new SFVertex3f(a.getV()[0], a.getV()[1], a.getV()[2]);

        //Note: this was v' = v + sin(alpha) d x v + (1-cos(alpha)) d x (d x v)

//		float x1=(float)(getV()[1]*a.getV()[2]-getV()[2]*a.getV()[1]);
//		float y1=(float)(getV()[2]*a.getV()[0]-getV()[0]*a.getV()[2]);
//		float z1=(float)(getV()[0]*a.getV()[1]-getV()[1]*a.getV()[0]);
//		
//		float x2=getV()[1]*z1-getV()[2]*y1;
//		float y2=getV()[2]*x1-getV()[0]*z1;
//		float z2=getV()[0]*y1-getV()[1]*x1;
//		
//		float wr=1-getV()[3];
//		
//		b.getV()[0]+=x1+wr*x2;
//		b.getV()[1]+=y1+wr*y2;
//		b.getV()[2]+=z1+wr*z2;

        //But the true formula that should be used is based on alpha/2
        /*
		 * v' = v 2 cos(alpha/2)sin(alpha/2) d x v + 2 sin^2(alpha/2) d x (d x d)
		 * 
		 * q = (sin(alpha/2)dx i + sin(alpha/2)dy j + sin(alpha/2)dz k + cos(alpha/2));
		 */

        float x1 = (float) (getV()[1] * a.getV()[2] - getV()[2] * a.getV()[1]);
        float y1 = (float) (getV()[2] * a.getV()[0] - getV()[0] * a.getV()[2]);
        float z1 = (float) (getV()[0] * a.getV()[1] - getV()[1] * a.getV()[0]);

        float x2 = getV()[1] * z1 - getV()[2] * y1;
        float y2 = getV()[2] * x1 - getV()[0] * z1;
        float z2 = getV()[0] * y1 - getV()[1] * x1;

        float w = getV()[3];

        b.getV()[0] += 2 * (x1 * w + x2);
        b.getV()[1] += 2 * (y1 * w + y2);
        b.getV()[2] += 2 * (z1 * w + z2);

        return b;
    }

    /**
     * Get the Rotation Matrix related to this quaternion
     *
     * @param q
     */
    public SFMatrix3f getRotationMatrix() {
        SFMatrix3f m = new SFMatrix3f();

        float x = getV()[0];
        float y = getV()[1];
        float z = getV()[2];
        float w = getV()[3];


        m.setA(1 - 2 * y * y - 2 * z * z);   //L'ultimo termine.. y( x ay - y ax ) - z*( z ax - x az )  = (-y*y -z*z)ax  +x*y ay + z*x az
        m.setB(+2 * x * y - 2 * z * w);
        m.setC(+2 * x * z + 2 * w * y);

//		m.setD(- 2*getV()[2]*getV()[3]+2*getV()[0]*getV()[1]);
//		m.setE(- 1 + 2*(getV()[0]*getV()[0] + getV()[2]*getV()[2])); //but why this?
//		m.setF(- 2*getV()[1]*getV()[2]-2*getV()[0]*getV()[3]);

        m.setD(+2 * x * y + 2 * z * w);
        m.setE(1 - 2 * x * x - 2 * z * z); //but why this?
        m.setF(2 * y * z - 2 * w * x);

        m.setG(-2 * w * y + 2 * x * z);
        m.setH(2 * y * z + 2 * w * x);
        m.setI(1 - 2 * x * x - 2 * y * y);

        return m;
    }

}
