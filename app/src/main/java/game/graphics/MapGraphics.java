package game.graphics;

import android.content.Context;
import java.util.List;
import game.physics.CollisionBox;
import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

public class MapGraphics {

    private Node rootNode = new Node();
    private Map map;

    public MapGraphics(Map map){
        this.map=map;
    }

    public void loadMap(Context context){
        List<Node> list=rootNode.getSonNodes();
        list.clear();
        for (CollisionBox[] boxes: map.getBoxes()){
            Model model=map.getBoxesGraphics(boxes).createModel(context);
            for (CollisionBox c: boxes){
                list.add(createNodeWithModel(model, c.getPos()));
            }
        }
    }

    public void draw() {
        rootNode.updateTree(new SFTransform3f());
        rootNode.draw();
    }

    private Node createNodeWithModel(Model model, SFVertex3f pos) {
        Node node = new Node();
        node.setModel(model);
        node.getRelativeTransform().setPosition(pos.getX(), pos.getY(), pos.getZ());
        return node;
    }

}
