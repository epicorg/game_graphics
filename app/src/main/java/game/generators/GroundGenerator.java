package game.generators;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import java.util.ArrayList;

import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.BitmapTexture;
import sfogl.integration.Material;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFImageFormat;

/**
 * Created by Andrea on 26/03/2015.
 */
public class GroundGenerator {

    private Model model;

    public GroundGenerator(Model model) {
        this.model = model;
    }

    public ArrayList<Node> getGround(int xCenter, int zCenter, int xSize, int zSize, int yCenter) {
        ArrayList<Node> tempList = new ArrayList<>();

        int leftToTheCenter = xSize / 2;
        int nearToTheCenter = zSize / 2;
        int tempX = xCenter - 2*leftToTheCenter;
        int tempZ = zCenter - 2*nearToTheCenter;

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < zSize; j++) {
                Node tmpNode = new Node();
                tmpNode.setModel(model);
                int xC = tempX + 2*i;
                int zC = tempZ + 2*j;
                tmpNode.getRelativeTransform().setPosition(xC, yCenter, zC);
                tempList.add(tmpNode);
            }
        }

        return tempList;
    }

}
