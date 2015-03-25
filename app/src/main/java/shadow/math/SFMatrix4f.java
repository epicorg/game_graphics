package shadow.math;


/**
 * A matrix of 4x4 elements as a set of 4 horizontal vector (a,b,c) (d,e,f) (g,h,i)
 *
 * @author Alessandro Martinelli
 * @template Data_Structure
 */
public class SFMatrix4f extends SFValue {

    public SFMatrix4f cloneValue() {
        return new SFMatrix4f(
                getV()[0], getV()[1], getV()[2], getV()[3],
                getV()[4], getV()[5], getV()[6], getV()[7],
                getV()[8], getV()[9], getV()[10], getV()[11],
                getV()[12], getV()[13], getV()[14], getV()[15]);
    }


    public SFMatrix4f() {
        getV()[0] = 1;
        getV()[5] = 1;
        getV()[10] = 1;
        getV()[15] = 1;
    }

    float[] v = new float[16];

    @Override
    public int getSize() {
        return 16;
    }

    @Override
    public float[] getV() {
        return v;
    }

    public SFMatrix4f(float a, float b, float c, float d,
                      float e, float f, float g, float h,
                      float i, float l, float m, float n,
                      float o, float p, float q, float r) {
        getV()[0] = a;
        getV()[1] = b;
        getV()[2] = c;
        getV()[3] = d;
        getV()[4] = e;
        getV()[5] = f;
        getV()[6] = g;
        getV()[7] = h;
        getV()[8] = i;
        getV()[9] = l;
        getV()[10] = m;
        getV()[11] = n;
        getV()[12] = o;
        getV()[13] = p;
        getV()[14] = q;
        getV()[15] = r;
    }

    public static SFMatrix4f getIdentity() {
        SFMatrix4f n = new SFMatrix4f(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );

        return n;
    }

    public static void toTransform3f(SFMatrix4f to, SFTransform3f from) {
        float[] v = from.getV();
        float[] toV = to.getV();

        toV[0] = v[0];
        toV[1] = v[3];
        toV[2] = v[6];
        toV[3] = 0;

        toV[4] = v[1];
        toV[5] = v[4];
        toV[6] = v[7];
        toV[7] = 0;

        toV[8] = v[2];
        toV[9] = v[5];
        toV[10] = v[8];
        toV[11] = 0;

        toV[12] = v[9];
        toV[13] = v[10];
        toV[14] = v[11];
        toV[15] = 1;
    }

    public static void mult(SFMatrix4f result, SFMatrix4f matrix1, SFMatrix4f matrix2) {
        float[] v = result.getV();
        float[] v1 = matrix1.getV();
        float[] v2 = matrix2.getV();

        v[0] = v1[0] * v2[0] + v1[4] * v2[1] + v1[8] * v2[2] + v1[12] * v2[3];
        v[1] = v1[1] * v2[0] + v1[5] * v2[1] + v1[9] * v2[2] + v1[13] * v2[3];
        v[2] = v1[2] * v2[0] + v1[6] * v2[1] + v1[10] * v2[2] + v1[14] * v2[3];
        v[3] = v1[3] * v2[0] + v1[7] * v2[1] + v1[11] * v2[2] + v1[15] * v2[3];

        v[4] = v1[0] * v2[4] + v1[4] * v2[5] + v1[8] * v2[6] + v1[12] * v2[7];
        v[5] = v1[1] * v2[4] + v1[5] * v2[5] + v1[9] * v2[6] + v1[13] * v2[7];
        v[6] = v1[2] * v2[4] + v1[6] * v2[5] + v1[10] * v2[6] + v1[14] * v2[7];
        v[7] = v1[3] * v2[4] + v1[7] * v2[5] + v1[11] * v2[6] + v1[15] * v2[7];

        v[8] = v1[0] * v2[8] + v1[4] * v2[9] + v1[8] * v2[10] + v1[12] * v2[11];
        v[9] = v1[1] * v2[8] + v1[5] * v2[9] + v1[9] * v2[10] + v1[13] * v2[11];
        v[10] = v1[2] * v2[8] + v1[6] * v2[9] + v1[10] * v2[10] + v1[14] * v2[11];
        v[11] = v1[3] * v2[8] + v1[7] * v2[9] + v1[11] * v2[10] + v1[15] * v2[11];

        v[12] = v1[0] * v2[12] + v1[4] * v2[13] + v1[8] * v2[14] + v1[12] * v2[15];
        v[13] = v1[1] * v2[12] + v1[5] * v2[13] + v1[9] * v2[14] + v1[13] * v2[15];
        v[14] = v1[2] * v2[12] + v1[6] * v2[13] + v1[10] * v2[14] + v1[14] * v2[15];
        v[15] = v1[3] * v2[12] + v1[7] * v2[13] + v1[11] * v2[14] + v1[15] * v2[15];
    }


    public static void multTransform(SFMatrix4f result, SFMatrix4f matrix1, SFTransform3f transform) {
        float[] v = result.getV();
        float[] v1 = matrix1.getV();
        float[] v2 = transform.getV();

        v[0] = v1[0] * v2[0] + v1[4] * v2[3] + v1[8] * v2[6];
        v[1] = v1[1] * v2[0] + v1[5] * v2[3] + v1[9] * v2[6];
        v[2] = v1[2] * v2[0] + v1[6] * v2[3] + v1[10] * v2[6];
        v[3] = v1[3] * v2[0] + v1[7] * v2[3] + v1[11] * v2[6];

        v[4] = v1[0] * v2[1] + v1[4] * v2[4] + v1[8] * v2[7];
        v[5] = v1[1] * v2[1] + v1[5] * v2[4] + v1[9] * v2[7];
        v[6] = v1[2] * v2[1] + v1[6] * v2[4] + v1[10] * v2[7];
        v[7] = v1[3] * v2[1] + v1[7] * v2[4] + v1[11] * v2[7];

        v[8] = v1[0] * v2[2] + v1[4] * v2[5] + v1[8] * v2[8];
        v[9] = v1[1] * v2[2] + v1[5] * v2[5] + v1[9] * v2[8];
        v[10] = v1[2] * v2[2] + v1[6] * v2[5] + v1[10] * v2[8];
        v[11] = v1[3] * v2[2] + v1[7] * v2[5] + v1[11] * v2[8];

        v[12] = v1[0] * v2[9] + v1[4] * v2[10] + v1[8] * v2[11] + v1[12];
        v[13] = v1[1] * v2[9] + v1[5] * v2[10] + v1[9] * v2[11] + v1[13];
        v[14] = v1[2] * v2[9] + v1[6] * v2[10] + v1[10] * v2[11] + v1[14];
        v[15] = v1[3] * v2[9] + v1[7] * v2[10] + v1[11] * v2[11] + v1[15];
    }
}
