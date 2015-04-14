package game.graphics;

import android.content.Context;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import sfogl.integration.Node;
import shadow.math.SFTransform3f;

public class Map {

    private LinkedList<MazeObject> list=new LinkedList<>();
    private Node mainNode=new Node();

    public void addObjects(MazeObject... ms){
        Collections.addAll(list,ms);
    }

    public void loadMap(CollisionMediator cm, Context context){
        List<Node> ln=mainNode.getSonNodes();
        for (MazeObject m: list){
            CollisionBox cb=m.getBox();
            if (cb!=null)
                cm.addObject(cb);
            ln.add(m.getNode(context));
        }
    }

    public void draw(){
        mainNode.updateTree(new SFTransform3f());
        mainNode.draw();
    }


}
