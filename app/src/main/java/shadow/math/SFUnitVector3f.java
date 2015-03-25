package shadow.math;


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
        float cosa = (float) (Math.cos(getV()[0]));
        float sina = (float) (Math.sin(getV()[0]));
        float cosb = (float) (Math.cos(getV()[1]));
        float sinb = (float) (Math.sin(getV()[1]));
        write.set3f(cosa, sina * cosb, sina * sinb);
    }

    /**
     * Set the 3D unit vector described by this Value.
     * If read is not a unit vector, it gets normalized before setting the values.
     *
     * @param read
     */
    public void setVertex3f(SFVertex3f read) {
        read.normalize3f();
        float cosa = read.getX();
        float sina = (float) (Math.sqrt(1 - cosa * cosa));

        getV()[0] = (float) (Math.atan2(sina, cosa));//SFStaticAnglesSet.getAngleslq().getIndexByTrigonometricValues(cosa, sina);
        float sinaRec = 1.0f / sina;
        float cosb = read.getY() * sinaRec;
        float sinb = read.getZ() * sinaRec;
        getV()[1] = (float) (Math.atan2(sinb, cosb));
    }

}
