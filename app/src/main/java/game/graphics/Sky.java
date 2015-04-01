package game.graphics;

import android.content.Context;

import com.example.alessandro.computergraphicsexample.R;

import game.generators.FundamentalGenerator;
import sfogl.integration.ArrayObject;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 31/03/2015.
 */
public class Sky {
    
    private static final int SKY_DISTANCE = 20;

    private Context context;
    private ShadingProgram program;
    private SFVertex3f position;

    private Node mainNode = new Node();

    public Sky(Context context, ShadingProgram program, SFVertex3f position) {
        this.program = program;
        this.context = context;
        this.position = position;

        setup();
    }

    private void setup(){
        mainNode.getSonNodes().add(generateNode(arrayObjectPosX, R.drawable.skybox_posx));
        mainNode.getSonNodes().add(generateNode(arrayObjectPosY, R.drawable.skybox_posy));
        mainNode.getSonNodes().add(generateNode(arrayObjectPosZ, R.drawable.skybox_negz));
        mainNode.getSonNodes().add(generateNode(arrayObjectNegX, R.drawable.skybox_negx));
        //mainNode.getSonNodes().add(generateNode(arrayObjectNegY, R.drawable.skybox_negy));
        mainNode.getSonNodes().add(generateNode(arrayObjectNegZ, R.drawable.skybox_posz));

        mainNode.getRelativeTransform().setPosition(0, 4, 0);
        mainNode.getRelativeTransform().setMatrix(SFMatrix3f.getIdentity());
    }

    private Node generateNode(ArrayObject arrayObject, int texture){
        Mesh meshPosX = new Mesh(arrayObject);
        meshPosX.init();
        Model modelPosX = new Model();
        modelPosX.setRootGeometry(meshPosX);
        modelPosX.setMaterialComponent(FundamentalGenerator.getMaterial(context, program, texture));
        Node nodePosX = new Node();
        nodePosX.setModel(modelPosX);

        return nodePosX;
    }

    public void draw(){
        mainNode.getRelativeTransform().setPosition(position.getX(), 4 + position.getY(), position.getZ());
        mainNode.updateTree(new SFTransform3f());
        mainNode.draw();
    }

    private ArrayObject arrayObjectPosX = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE
            },
            new float[]{
            },
            new float[]{
                    0,0,0,
                    1,0,0,
                    1,1,0,
                    0,0,0,
                    0,1,0,
                    1,1,0
            },
            new short[]{
                    0,1,2,3,4,5
            }
    );
    private ArrayObject arrayObjectPosY = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE
            },
            new float[]{
            },
            new float[]{
                    1,0,0,
                    1,1,0,
                    0,0,0,
                    0,0,0,
                    1,1,0,
                    0,1,0
            },
            new short[]{
                    0,1,2,3,4,5
            }
    );
    private ArrayObject arrayObjectPosZ = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE
            },
            new float[]{
            },
            new float[]{
                    0,0,0,
                    0,1,0,
                    1,0,0,
                    1,0,0,
                    0,1,0,
                    1,1,0
            },
            new short[]{
                    0,1,2,3,4,5
            }
    );
    private ArrayObject arrayObjectNegX = new ArrayObject(
            new float[]{
                    -SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, +SKY_DISTANCE
            },
            new float[]{
            },
            new float[]{
                    1,0,0,
                    0,0,0,
                    0,1,0,
                    1,0,0,
                    1,1,0,
                    0,1,0
            },
            new short[]{
                    0,1,2,3,4,5
            }
    );
    /*private ArrayObject arrayObjectNegY = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, +SKY_DISTANCE,
                    +SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE
            },
            new float[]{
            },
            new float[]{
                    1,0,0,
                    1,1,0,
                    0,0,0,
                    0,0,0,
                    1,1,0,
                    0,1,0
            },
            new short[]{
                    0,1,2,3,4,5
            }
    );*/
    private ArrayObject arrayObjectNegZ = new ArrayObject(
            new float[]{
                    +SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, -SKY_DISTANCE, -SKY_DISTANCE,
                    +SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE,
                    -SKY_DISTANCE, +SKY_DISTANCE, -SKY_DISTANCE
            },
            new float[]{
            },
            new float[]{
                    1,0,0,
                    1,1,0,
                    0,0,0,
                    0,0,0,
                    1,1,0,
                    0,1,0
            },
            new short[]{
                    0,1,2,3,4,5
            }
    );

}
