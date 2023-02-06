package graphic.integration;

import java.util.LinkedList;
import java.util.List;

import graphic.shadow.math.SFValue;

/**
 * An instance of a Shading Program with assigned values
 *
 * @author Alessandro Martinelli
 */
public class Material {

    private SFValue[] values = new SFValue[0];
    private final List<BitmapTexture> textures = new LinkedList<>();
    private final ShadingProgram program;

    public Material(ShadingProgram program) {
        this.program = program;
    }

    public SFValue[] getValues() {
        return values;
    }

    public Material clone() {
        Material element = new Material(program);
        element.values = values;
        element.textures.addAll(textures);
        return element;
    }

    public void loadData() {
        program.setData(values);
        for (int i = 0; i < textures.size(); i++)
            program.setTextureData(i, textures.get(i).getTexture());
    }

    public void setData(SFValue[] reference) {
        this.values = reference;
    }

    public ShadingProgram getProgram() {
        return program;
    }

    public void addTexture(BitmapTexture texture) {
        textures.add(texture);
    }

    public List<BitmapTexture> getTextures() {
        return textures;
    }

}
