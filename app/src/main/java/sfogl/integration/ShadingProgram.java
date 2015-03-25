package sfogl.integration;

import android.util.Log;

import java.util.ArrayList;

import sfogl.integration.ShadingParameter.ParameterType;
import sfogl2.SFOGLShader;
import sfogl2.SFOGLTexture2D;
import shadow.math.SFValue;
import shadow.system.SFInitiable;

public class ShadingProgram implements SFInitiable {

    public static final int PROJECTION_POSITION = 0;
    public static final int MODELVIEW_POSITION = 1;

    public static final int VERTICES_INDEX = 0;
    public static final int NORMALS_INDEX = 1;
    public static final int TXCOORD_INDEX = 2;

    public static final int POSITION_OF_PARAMETERS = 3;

    private SFOGLShader shader;

    private int[] materialTextures;

    public ShadingProgram(SFOGLShader shader) {
        super();
        this.shader = shader;
    }

    public SFOGLShader getShader() {
        return shader;
    }

    @Override
    public void init() {
        Log.e("ShadingProgram", "Compile With Data Data");
        shader.compileShaderWithInfos(System.out);
        shader.compileData();
    }

    @Override
    public void destroy() {

    }

    public void loadData(ShadingParameter[] parameters) {
        ArrayList<String> uniformNames = new ArrayList<String>();
        uniformNames.add("projection");
        uniformNames.add("transform");
        uniformNames.add("vTransform");

        ArrayList<Integer> textures = new ArrayList<Integer>();

        for (int k = 0; k < parameters.length; k++) {
            ShadingParameter param = parameters[k];
            if (param.getType() == ParameterType.GLOBAL_TEXTURE) {
                textures.add(uniformNames.size());
            }
            uniformNames.add(param.getName());
        }

        materialTextures = new int[textures.size()];
        for (int i = 0; i < materialTextures.length; i++) {
            materialTextures[i] = textures.get(i);
        }

        shader.setAttribs("position", "normal", "txCoord");
        shader.setUniforms(uniformNames.toArray(new String[uniformNames.size()]));
    }

    public void setTransform(float[] transform) {
        shader.setUniformValue(MODELVIEW_POSITION, transform);
    }

    public void setupProjection(float projection[]) {
        shader.setUniformValue(PROJECTION_POSITION, projection);
    }

    public void setData(SFValue[] data) {
        for (int i = 0; i < data.length; i++) {
            SFValue v = data[i];
            shader.setUniformValue(POSITION_OF_PARAMETERS + i, v.getV());
        }
    }

    public void setTextureData(int index, SFOGLTexture2D texture) {
        texture.apply(index);
        shader.setUniformValuei(materialTextures[index], index);
    }

}
