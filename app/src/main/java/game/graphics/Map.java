package game.graphics;

import android.content.Context;

import com.example.alessandro.computergraphicsexample.ShadersKeeper;

import java.util.HashMap;

import game.generators.FundamentalGenerator;
import game.physics.Box;
import game.physics.Collidable;
import game.physics.CollisionMediator;
import game.physics.Wall;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;

public class Map {

    private HashMap<Collidable, Model> map = new HashMap<Collidable, Model>();
    private Node rootNode;
    private Context context;

    public Map(Context context) {
        this.context = context;
    }

    public void addObjects(String obj, int texture_id, Collidable... collidables) {
        ShadingProgram program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
        Model model = FundamentalGenerator.getModel(context, program, texture_id, obj);
        for (Collidable c : collidables) {
            map.put(c, model);
        }
    }

    public void load(CollisionMediator cm) {
        rootNode = new Node();
        rootNode.setup();
        for (Collidable c : map.keySet()) {
            cm.addObject(c);
            if (c instanceof Wall) {
                Wall w = (Wall) c;
                Node node = new Node();
                node.setModel(map.get(c));
                Box b = (Box) w.getBox();
                node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(b.getWidth() / 2, b.getLength() / 2, b.getHeight() / 2));
                node.getRelativeTransform().setPosition(w.getPos().getX(), w.getPos().getY()-b.getLength()/2, w.getPos().getZ());
                rootNode.getSonNodes().add(node);
            }

        }
    }

    public void draw() {
        rootNode.updateTree(new SFTransform3f());
        rootNode.draw();
    }
}
