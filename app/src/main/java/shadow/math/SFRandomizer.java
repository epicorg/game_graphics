package shadow.math;

/**
 * A module generating random values.
 * Once initialized with the same seed, it will always generate the same set of values. 
 * 
 * @template Tool Template
 * 
 * @author Alessandro
 */
public class SFRandomizer {

	private static final int a = 40;
	private static final int b = 1000000;
	private static final int size = b+1;
	private static final int beginTimes = 4;
	private static final float step = 1.0f / b;

	private int seed, value;

	/**
	 * Generates this randomizer given the seed.
	 * @param seed
	 */
	public SFRandomizer(int seed) {
		super();
		this.seed = seed % size;
		reset();
		for (int i = 0; i < beginTimes; i++) {
			randomInt();
		}
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}
	
	/**
	 * Generates a random integer
	 * @return
	 */
	public int randomInt() {
		//Linear congruential generator
		value = (a * value + b) % size;
		return value;
	}
	
	/**
	 * Generates a random float
	 * @return
	 */
	public float randomFloat() {
		return randomInt() * step;
	}

	/**
	 * reset this randomizer. 
	 * @return
	 */
	public void reset() {
		value = seed;
	}


}
