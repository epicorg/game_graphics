package game.graphics;

import android.content.Context;

import com.example.alessandro.computergraphicsexample.ShadersKeeper;

import game.physics.CollisionBox;
import game.physics.Square;
import sfogl.integration.Node;
import shadow.math.SFVertex3f;

/**
 * Created by depa on 14/04/15.
 */
public class Wall implements MazeObject{

    private Square box;
    private int textureID;

    public Wall(Square box, int textureID){
        this.box=box;
        this.textureID=textureID;
    }

    public Node getNode(Context context){
        Node node = new Node();
        node.setModel(new WallGenerator(context,ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER),
                textureID, box.getxSize(), box.getySize(), box.getzSize()).getModel());
        SFVertex3f position=box.getPos();
        node.getRelativeTransform().setPosition(position.getX(),position.getY(),position.getZ());
        return node;
    }

    public CollisionBox getBox(){
        return box;
    }

}
