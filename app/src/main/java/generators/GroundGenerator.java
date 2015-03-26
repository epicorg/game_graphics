package generators;

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

    private Context context;
    private ShadingProgram program;
    private Model groundModel;

    public GroundGenerator(Context context, ShadingProgram program, int textureId, String obj) {
        this.context = context;
        this.program = program;

        setup(textureId, obj);
    }

    private void setup(int textureId, String obj) {
        int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
        BitmapTexture groundTexture = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(), textureId), textureModel);
        groundTexture.init();
        ArrayObject[] groundObject = ObjLoader.arrayObjectFromFile(context, obj);
        Mesh groundMesh = new Mesh(groundObject[0]);
        groundMesh.init();
        Material groundMaterial = new Material(program);
        groundMaterial.getTextures().add(groundTexture);
        groundModel = new Model();
        groundModel.setRootGeometry(groundMesh);
        groundModel.setMaterialComponent(groundMaterial);
    }

    public ArrayList<Node> getGround(int xCenter, int zCenter, int xSize, int zSize, int yCenter) {
        ArrayList<Node> tempList = new ArrayList<>();

        int leftToTheCenter = xSize / 2;
        int nearToTheCenter = zSize / 2;
        int tempX = xCenter - leftToTheCenter;
        int tempZ = zCenter - nearToTheCenter;

        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < zSize; j++) {
                Node tmpNode = new Node();
                tmpNode.setModel(groundModel);
                int xC = tempX + i;
                int zC = tempZ + j;
                tmpNode.getRelativeTransform().setPosition(xC, yCenter, zC);
                tempList.add(tmpNode);
            }
        }

        return tempList;
    }

}
