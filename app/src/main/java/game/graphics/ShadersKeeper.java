package game.graphics;

import android.content.Context;

import java.util.HashMap;

import graphic.integration.Shaders;
import graphic.integration.ShadingParameter;
import graphic.integration.ShadingProgram;

/**
 * @author Alessandro Martinelli
 * @date 16/03/2015
 */
public class ShadersKeeper {

    public static final String STANDARD_TEXTURE_SHADER = "reflect";

    private static final HashMap<String, ShadingProgram> shaders = new HashMap<>();

    private static final ShadingParameter pTextureMaterial = new ShadingParameter("textureMaterial", ShadingParameter.ParameterType.GLOBAL_TEXTURE);
    //private static final ShadingParameter pColor = new ShadingParameter("color", ShadingParameter.ParameterType.GLOBAL_FLOAT4);
    //private static final ShadingParameter baseColor = new ShadingParameter("baseColor", ShadingParameter.ParameterType.GLOBAL_FLOAT4);
    //private static final ShadingParameter specColor = new ShadingParameter("specColor", ShadingParameter.ParameterType.GLOBAL_FLOAT4);
    //private static final ShadingParameter lPos1 = new ShadingParameter("lighPos1", ShadingParameter.ParameterType.GLOBAL_FLOAT3);
    //private static final ShadingParameter lPos2 = new ShadingParameter("lighPos2", ShadingParameter.ParameterType.GLOBAL_FLOAT3);
    //private static final ShadingParameter lPos3 = new ShadingParameter("lighPos3", ShadingParameter.ParameterType.GLOBAL_FLOAT3);
    //private static final ShadingParameter sh = new ShadingParameter("sh", ShadingParameter.ParameterType.GLOBAL_FLOAT);
    //private static final ShadingParameter reflect = new ShadingParameter("reflect", ShadingParameter.ParameterType.GLOBAL_FLOAT);
    //private static final ShadingParameter pTextureLow = new ShadingParameter("textureLow", ShadingParameter.ParameterType.GLOBAL_TEXTURE);
    //private static final ShadingParameter pTextureNormal = new ShadingParameter("textureNormal", ShadingParameter.ParameterType.GLOBAL_TEXTURE);
    //private static final ShadingParameter pTextureHigh = new ShadingParameter("textureHigh", ShadingParameter.ParameterType.GLOBAL_TEXTURE);

    public static void loadPipelineShaders(Context context) {
        String vertexShader = Shaders.loadText(context, "shaders/stdVertexShader.vsh");
        String fragmentShader = Shaders.loadText(context, "shaders/stdFragmentShader.fsh");

        ShadingProgram program = Shaders.loadShaderModel(vertexShader, fragmentShader, pTextureMaterial);
        program.init();
        shaders.put(STANDARD_TEXTURE_SHADER, program);
    }

    public static ShadingProgram getProgram(String name) {
        return shaders.get(name);
    }

    public static void clear() {
        shaders.clear();
    }

}
