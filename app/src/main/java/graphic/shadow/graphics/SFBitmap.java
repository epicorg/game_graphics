package graphic.shadow.graphics;

import java.nio.ByteBuffer;

/**
 * SFBitmap requires some image generation process to define image data, which are stored as a ByteBuffer.
 * If data have not been defined, the 'data' field is null.
 */
public class SFBitmap {

    /**
     * width of the image in pixels
     */
    private int width;
    /**
     * height of the image in pixels
     */
    private int height;
    /**
     * format of the image
     */
    private SFImageFormat format = SFImageFormat.RGBA;
    /**
     * data type depends on Image generation mode
     */
    private ByteBuffer data;

    public SFBitmap() {
        super();
    }

    /**
     * Generate an image with the desired with and height.
     * Default format is RGB.
     *
     * @param width
     * @param height
     * @return the image being generated.
     */
    public static SFBitmap generateRGBImage(int width, int height) {
        SFBitmap ret = new SFBitmap();
        ret.setWidth(width);
        ret.setHeight(height);
        return ret;
    }

    /**
     * Generate an image with the desired with and height.
     *
     * @param width  width of the image
     * @param height height of the image
     * @param format format of the image
     * @return the image being generated.
     */
    public static SFBitmap generateRGBImage(int width, int height, SFImageFormat format) {
        SFBitmap ret = new SFBitmap();
        ret.setWidth(width);
        ret.setHeight(height);
        ret.setFormat(format);
        return ret;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getGray(int x, int y) {
        byte b = data.get(getSize() * (x + width * y));
        return (b >= 0 ? b : b + 256);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public SFImageFormat getFormat() {
        return format;
    }

    public void setFormat(SFImageFormat format) {
        this.format = format;
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    public int getSize() {
        int size = 1;
        if (getFormat() == SFImageFormat.RGB) {
            size = 3;
        }
        return size;
    }

    public void generateBitmap(int width, int height, int[] values, SFImageFormat format) {

        if (data != null)
            data.clear();

        setWidth(width);
        setHeight(height);
        setFormat(format);

        int size = 1;
        switch (format) {
            case RGB565:
            case RGB:
                size = 3;
                break;
            case RGBA4:
            case RGBA:
                size = 4;
            default:
        }

        int checkSize = 1;
        switch (format) {
            case RGB565:
            case RGBA4:
                checkSize = 2;
                break;
            case RGB:
                checkSize = 3;
                break;
            case RGBA:
                checkSize = 4;
                break;
            default:
        }

        if (width * height * checkSize != values.length)
            throw new RuntimeException("An SFBitmapArrayData must have an array of " + (width * height * checkSize) +
                    " values, there are " + values.length + "values");

        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * size);

        if (format == SFImageFormat.GRAY || format == SFImageFormat.RGB || format == SFImageFormat.RGBA) {
            for (int value : values)
                buffer.put((byte) value);
        } else if (format == SFImageFormat.RGBA4) {
            for (int value : values) {
                int values1 = (value & 0xf0);
                int values2 = (value & 0xf) << 4;
                buffer.put((byte) (values1));
                buffer.put((byte) (values2));
            }
            setFormat(SFImageFormat.RGBA);
        } else if (format == SFImageFormat.RGB565) {
            for (int i = 0; i < values.length; i += 2) {
                int v1 = values[i];
                int v2 = values[i + 1];
                int values1 = (v1 & 248);
                int values2 = ((v1 & 7) << 5) + ((v2 & 224) >> 3);
                int values3 = (v2 & 31) << 3;
                buffer.put((byte) (values1));
                buffer.put((byte) (values2));
                buffer.put((byte) (values3));
            }
            setFormat(SFImageFormat.RGB);
        }

        setData(buffer);
        buffer.rewind();
    }

}
