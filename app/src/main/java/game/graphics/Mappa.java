package game.graphics;

import android.content.Context;
import com.example.alessandro.computergraphicsexample.ShadersKeeper;

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

import java.util.HashMap;

public class Mappa {

    private HashMap<Collidable,Model> map=new HashMap<Collidable,Model>();
    private Node rootNode;
    private Context context;

    public Mappa(Context context){
        this.context=context;
    }

    public void addObjects(String obj, int texture_id, Collidable... collidables){
        ShadingProgram program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
        Model model= FundamentalGenerator.loadModel(context, program, texture_id, obj);
        for (Collidable c: collidables){
            map.put(c,model);
        }
    }

    public void load(CollisionMediator cm){
        rootNode=new Node();
        rootNode.setup();
        for (Collidable c : map.keySet()){
            cm.addObject(c);
            if (c instanceof  Wall){
                Wall w=(Wall) c;
                Node node = new Node();
                node.setModel(map.get(c));
                Box b=(Box) w.getBox();
                node.getRelativeTransform().setMatrix(SFMatrix3f.getScale(b.getWidth(),b.getHeight(),b.getLength()));
                node.getRelativeTransform().setPosition(w.getPos().getX(), w.getPos().getY(), w.getPos().getZ());
                rootNode.getSonNodes().add(node);
            }

        }
    }

    public void draw() {
        rootNode.updateTree(new SFTransform3f());
        rootNode.draw();
    }
}
