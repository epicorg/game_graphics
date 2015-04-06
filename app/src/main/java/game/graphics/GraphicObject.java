package game.graphics;

import android.content.Context;
import com.example.alessandro.computergraphicsexample.ShadersKeeper;
import game.generators.FundamentalGenerator;
import sfogl.integration.Model;
import sfogl.integration.ShadingProgram;

/**
 * Created by depa on 06/04/15.
 */
public class GraphicObject {

    private int textureId;
    private String obj;

    public GraphicObject(int textureId, String obj){
        this.textureId=textureId;
        this.obj=obj;
    }

    public Model createModel(Context context){
        ShadingProgram program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
        return FundamentalGenerator.getModel(context, program, textureId, obj);
    }
}
