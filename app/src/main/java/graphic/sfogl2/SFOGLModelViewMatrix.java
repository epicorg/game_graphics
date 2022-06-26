package graphic.sfogl2;

import java.util.LinkedList;

import graphic.shadow.math.SFMatrix3f;
import graphic.shadow.math.SFVertex3f;

public class SFOGLModelViewMatrix {

    private final SFMatrix3f matrix = new SFMatrix3f();
    private final SFVertex3f position = new SFVertex3f();

    private final LinkedList<SFMatrix3f> stackM = new LinkedList<SFMatrix3f>();
    private final LinkedList<SFVertex3f> stackP = new LinkedList<SFVertex3f>();

    public SFOGLModelViewMatrix() {
        loadIdentity();
    }

    public void loadIdentity() {
        matrix.set(SFMatrix3f.getIdentity());
        position.set3f(0, 0, 0);
    }

    private void applyM(SFMatrix3f m) {
        /* Implements M=(M*(newM*P))+T */
        matrix.set(matrix.multMatrix(m));
    }

    public void rotateX(float angle) {
        SFMatrix3f m = SFMatrix3f.getRotationX(angle);
        applyM(m);
    }

    public void rotateY(float angle) {
        SFMatrix3f m = SFMatrix3f.getRotationY(angle);
        applyM(m);
    }

    public void rotateZ(float angle) {
        SFMatrix3f m = SFMatrix3f.getRotationZ(angle);
        applyM(m);
    }

    public void scale(float scaleX, float scaleY, float scaleZ) {
        SFMatrix3f m = SFMatrix3f.getScale(scaleX, scaleY, scaleZ);
        applyM(m);
    }

    public void translate3f(float x, float y, float z) {
        /* Implements M=(M*(P+newT))+T
         *
         * T' = M*newT+T
         */
        position.add(this.matrix.mult(new SFVertex3f(x, y, z)));
    }

    public void push() {
        SFMatrix3f m = new SFMatrix3f();
        m.set(this.matrix);
        stackM.push(m);
        SFVertex3f v = new SFVertex3f();
        v.set(position);
        stackP.push(v);
    }

    public void pop() {
        SFMatrix3f m = stackM.pop();
        SFVertex3f v = stackP.pop();
        if (m != null && v != null) {
            this.matrix.set(m);
            this.position.set(v);
        }
    }

    public float[] asOpenGLMatrix() {
        return new float[]{
                matrix.getA(), matrix.getD(), matrix.getG(), 0,
                matrix.getB(), matrix.getE(), matrix.getH(), 0,
                matrix.getC(), matrix.getF(), matrix.getI(), 0,
                position.getX(), position.getY(), position.getZ(), 1
        };
    }

}
