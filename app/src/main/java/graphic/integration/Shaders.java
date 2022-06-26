package graphic.integration;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import graphic.sfogl2.SFOGLShader;

public class Shaders {

    public static ShadingProgram loadShaderModel(String vertexShader, String fragmentShaderFile, ShadingParameter... parameters) {
        SFOGLShader shader = new SFOGLShader(vertexShader, fragmentShaderFile);
        ShadingProgram program = new ShadingProgram(shader);
        program.loadData(parameters);
        program.init();
        return program;
    }

    public static String loadText(Context context, String filename) {
        String text = "";

        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            line = reader.readLine();
            while (line != null) {

                text += line + "\n";

                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

}
