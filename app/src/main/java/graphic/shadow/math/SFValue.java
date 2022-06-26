package graphic.shadow.math;

import androidx.annotation.NonNull;

public abstract class SFValue {

    /**
     * Get the content vector for this value
     */
    public abstract float[] getV();

    /**
     * Get the size of this vector
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
    public void addMultiply(float m, SFValue value)
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
     * @param m the Value to be Added
     * @throws ArrayIndexOutOfBoundsException
     */
    public void multiply(float m) {
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
    public void multiplyValue(SFValue value)
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
        System.arraycopy(floatValueArray, size * index, v, 0, v.length);
    }

    public void get(int index, float[] floatValueArray) {
        int size = getSize();
        float[] v = getV();
        System.arraycopy(v, 0, floatValueArray, size * index, v.length);
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

    @NonNull
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[");
        for (int i = 0; i < getV().length - 1; i++) {
            s.append(getV()[i]).append(", ");
        }
        s.append(getV()[getV().length - 1]).append("]");
        return s.toString();
    }

}
