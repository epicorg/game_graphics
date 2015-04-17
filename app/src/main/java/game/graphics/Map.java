package game.graphics;

import android.content.Context;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import sfogl.integration.Node;
import shadow.math.SFTransform3f;

/**
 * Mappa che raccoglie i MazeObject, ha il compito di caricarli nel sistema di collisioni e rappresentarli.
 * @author Stefano De Pace
 *
 */
public class Map {

    private LinkedList<MazeObject> list=new LinkedList<>();
    private Node mainNode=new Node();

    /**
     * Aggiunge una serie di MazeObject alla Map.
     * @param ms uno o più MazeObject da aggiungere alla Map.
     */
    public void addObjects(MazeObject... ms){
        Collections.addAll(list,ms);
    }

    /**
     * Carica le CollisionBox dei MazeObject che non sono null nel CollisionMediator dato, e
     * carica i loro Node per il rendering.
     * @param cm CollisionMediator dove inserire le CollisionBox dei MazeObject.
     * @param context Context da cui ricavare le risorse per rappresentare i MazeObject.
     */
    public void loadMap(CollisionMediator cm, Context context){
        List<Node> ln=mainNode.getSonNodes();
        for (MazeObject m: list){
            CollisionBox cb=m.getBox();
            if (cb!=null)
                cm.addObject(cb);
            ln.add(m.getNode(context));
        }
    }

    /**
     * Disegna la Map, quindi tutti i nodi associati ad ogni MazeObject.
     */
    public void draw(){
        mainNode.updateTree(new SFTransform3f());
        mainNode.draw();
    }


}
