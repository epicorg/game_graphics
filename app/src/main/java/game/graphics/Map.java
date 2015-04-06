package game.graphics;

import android.content.Context;
import java.util.HashMap;
import game.physics.CollisionBox;
import game.physics.CollisionMediator;
import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

public class Map {

    private Context context;

    private Node rootNode = new Node();
    private HashMap<CollisionBox[],GraphicObject> map=new HashMap<>();

    public Map(Context context) {
        this.context = context;
    }

    public void addObjects(String obj, int textureId, CollisionBox... boxes) {
        map.put(boxes,new GraphicObject(textureId,obj));
    }

    public void loadMap(CollisionMediator cm, boolean graphics){
        for (CollisionBox[] boxes: map.keySet()){
            Model model=null;
            if (graphics) {
                model= map.get(boxes).createModel(context);
                rootNode.getSonNodes().clear();
            }
            for (CollisionBox c: boxes){
                if (cm!=null)
                    c.add(cm);
                if (graphics)
                    rootNode.getSonNodes().add(createNodeWithModel(model, c.getPos()));
            }
        }
    }

    private Node createNodeWithModel(Model model, SFVertex3f pos) {
        Node node = new Node();
        node.setModel(model);
        node.getRelativeTransform().setPosition(pos.getX(), pos.getY(), pos.getZ());
        return node;
    }

    public void draw() {
        rootNode.updateTree(new SFTransform3f());
        rootNode.draw();
    }

}
