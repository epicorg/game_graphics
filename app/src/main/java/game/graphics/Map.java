package game.graphics;

import android.content.Context;
import java.util.HashMap;
import java.util.List;
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

    public void loadMapLogic(CollisionMediator cm){
        for (CollisionBox[] boxes: map.keySet()){
            for (CollisionBox c: boxes){
                    c.add(cm);
            }
        }
    }

    public void loadMapGraphics(){
        List<Node> list=rootNode.getSonNodes();
        list.clear();
        for (CollisionBox[] boxes: map.keySet()){
            Model model=map.get(boxes).createModel(context);
            for (CollisionBox c: boxes){
                list.add(createNodeWithModel(model, c.getPos()));
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
