package shadow.math;

/**
 * A N-sized vector.
 * 
 * This is the base class for most of the ShadowFramework Data Structures and
 * contains the general implementation of array manipulation methods.
 * 
 * @template Data_Structure
 * 
 * @author Alessandro Martinelli
 */
public class SFValuenf extends SFValue{

	/**
	 * Generate the middle Point between two poits A and B
	 * 
	 * @param A
	 *            the first Point
	 * @param B
	 *            the second Point
	 * @return the middle Point
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static SFValuenf middle(SFValuenf A, SFValuenf B)
			throws ArrayIndexOutOfBoundsException {
		SFValuenf value = new SFValuenf(A.getV().length);
		for (int i = 0; i < A.getV().length; i++) {
			value.getV()[i] = (A.getV()[i] + B.getV()[i]) * 0.5f;
		}
		return value;
	}

	private float[] v;

	/**
	 * Generate a new SFValuenf, the internal value is set to null
	 * 
	 * @param n
	 */
	public SFValuenf() {
		super();
	}

	/**
	 * Generate a new SFValuenf given its size
	 * 
	 * @param n
	 */
	public SFValuenf(int n) {
		super();
		this.v=(new float[n]);
	}

	/**
	 * Generate a new SFValuenf given its vector
	 * 
	 * @param n
	 */
	public SFValuenf(float[] v) {
		super();
		this.v=(v);
	}

	/**
	 * @deprecated please, create a new vertex than call set or set2f
	 *
	 * Generate a clone of this Value
	 * 
	 * @param value
	 *            the Value to be Added
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public SFValuenf cloneValue() {
		SFValuenf v = new SFValuenf(this.getV().length);
		v.set(this);
		return v;
	}
	
	@Override
	public int getSize() {
		return v.length;
	}
	
	@Override
	public float[] getV() {
		return v;
	}
}