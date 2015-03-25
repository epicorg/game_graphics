package shadow.graphics;

public enum SFImageFormat {

    ALPHA, //(only alpha component, 8 bits)

    GRAY, //(rgb componentsm 1 value, 8 bits)
    GRAY_ALPHA, //(rgb components and alpha, 1 value + 1 value, 16 bits)

    RGB, //(rgb components, 3 values, 24 bits)
    RGB565, //(rgb components, 3 values, 16 bits)

    RGBA, //(rgb components, 4 values, 32 bits)
    RGBA4, //(rgb components, 4 values, 16 bits)
    RGBA5551; //(rgb components, 4 values, 16 bits)


    public static String[] getNames() {
        String[] names = new String[SFImageFormat.values().length];
        for (int i = 0; i < names.length; i++) {
            names[i] = values()[i].name();
        }
        return names;
    }
}
