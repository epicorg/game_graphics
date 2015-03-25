package shadow.math;

public abstract class SFValue {

    /**
     * Get the content vector for this value
     *
     * @param value
     * @return
     */
    public abstract float[] getV();

    /**
     * Get the size of this vector
     *
     * @param value
     * @return
     */
    public abstract int getSize();


    /**
     * Sum operation. This = This + value
     *
     * @param value the Value to be Added
     * @throws ArrayIndexOutOfBoundsException
     */
    public void add(SFValue value) throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < getV().length; i++) {
            getV()[i] += value.getV()[i];
        }
    }

    /**
     * Sum operation, with a multiplication factor. This = This + m * value
     *
     * @param value the Value to be Added
     * @throws ArrayIndexOutOfBoundsException
     */
    public void addMult(float m, SFValue value)
            throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < getV().length && i < value.getV().length; i++) {
            getV()[i] += m * value.getV()[i];
        }
    }

    /**
     * Generate the dot product of this value with another value,
     *
     * @param value
     * @return
     */
    public float dot(SFValue value) throws ArrayIndexOutOfBoundsException {
        float dot = 0;
        for (int i = 0; i < getV().length; i++) {
            dot += getV()[i] * value.getV()[i];
        }
        return dot;
    }


    /**
     * Multiplication operation. This = m * value
     *
     * @param value the Value to be Added
     * @throws ArrayIndexOutOfBoundsException
     */
    public void mult(float m) {
        for (int i = 0; i < getV().length; i++) {
            getV()[i] *= m;
        }
    }

    /**
     * Multiplication operation with another value. Must be of the same size. <br/>
     * This[i] = This[i] * value[i]
     *
     * @param value the Value to be Multiplied
     * @throws ArrayIndexOutOfBoundsException
     */
    public void multValue(SFValue value)
            throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < getV().length; i++) {
            getV()[i] *= value.getV()[i];
        }
    }

    /**
     * Set operation with another value. <br/>
     * This[i] = value[i]
     *
     * @param value the Value to be Set
     * @throws ArrayIndexOutOfBoundsException
     */
    public void set(SFValue value) throws ArrayIndexOutOfBoundsException {
        for (int i = 0; i < getV().length && i < value.getV().length; i++) {
            getV()[i] = value.getV()[i];
        }
    }

    /**
     * Set the index-th element of the value <br/>
     * This[index] = value
     *
     * @param element the Value to be Set
     * @throws ArrayIndexOutOfBoundsException
     */
    public void setByIndex(int index, float element)
            throws ArrayIndexOutOfBoundsException {
        getV()[index] = element;
    }

    /**
     * Set the all the elements of this Array
     *
     * @param elements
     */
    public void setArray(float[] elements) {
        for (int i = 0; i < elements.length; i++) {
            getV()[i] = elements[i];
        }
    }

    public void set(int index, float[] floatValueArray) {
        int size = getSize();
        float[] v = getV();
        for (int j = 0; j < v.length; j++) {
            v[j] = floatValueArray[size * index + j];
        }
    }

    public void get(int index, float[] floatValueArray) {
        int size = getSize();
        float[] v = getV();
        for (int j = 0; j < v.length; j++) {
            floatValueArray[size * index + j] = v[j];
        }
    }

    /**
     * Subtraction. This = This - value
     *
     * @param value the Value to be Added
     * @throws ArrayIndexOutOfBoundsException
     */
    public void subtract(SFValue value) {
        for (int i = 0; i < getV().length; i++) {
            getV()[i] -= value.getV()[i];
        }
    }

    @Override
    public String toString() {
        String s = "[";
        for (int i = 0; i < getV().length - 1; i++) {
            s += getV()[i] + ", ";
        }
        s += getV()[getV().length - 1] + "]";
        return s;
    }
}
