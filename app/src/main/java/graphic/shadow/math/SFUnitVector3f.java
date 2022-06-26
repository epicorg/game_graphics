package graphic.shadow.math;


/**
 * A set of 2 angles describing and 3D unit vector
 *
 * @author Alessandro Martinelli
 * @pattern Data_Extension
 */
public class SFUnitVector3f extends SFVertex2f {

    public SFUnitVector3f() {
        super(0, 0);
    }

    /**
     * Get the 3D unit vector described by this Value
     *
     * @param write an SFVertex3f where the value will be written
     */
    public void getVertex3f(SFVertex3f write) {
        float cosA = (float) (Math.cos(getV()[0]));
        float sinA = (float) (Math.sin(getV()[0]));
        float cosB = (float) (Math.cos(getV()[1]));
        float sinB = (float) (Math.sin(getV()[1]));
        write.set3f(cosA, sinA * cosB, sinA * sinB);
    }

    /**
     * Set the 3D unit vector described by this Value.
     * If read is not a unit vector, it gets normalized before setting the values.
     */
    public void setVertex3f(SFVertex3f read) {
        read.normalize3f();
        float cosA = read.getX();
        float sinA = (float) (Math.sqrt(1 - cosA * cosA));

        getV()[0] = (float) (Math.atan2(sinA, cosA));//SFStaticAnglesSet.getAngleslq().getIndexByTrigonometricValues(cosA, sinA);
        float sinaRec = 1.0f / sinA;
        float cosB = read.getY() * sinaRec;
        float sinB = read.getZ() * sinaRec;
        getV()[1] = (float) (Math.atan2(sinB, cosB));
    }

}
