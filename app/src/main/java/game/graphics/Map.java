package game.graphics;

import android.content.Context;

import com.example.alessandro.computergraphicsexample.ShadersKeeper;

import game.generators.FundamentalGenerator;
import game.physics.Circle;
import game.physics.CollisionMediator;
import game.physics.Square;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import shadow.math.SFTransform3f;

public class Map {

    private Context context;
    private CollisionMediator collisionMediator;

    private Node rootNode = new Node();

    public Map(Context context, CollisionMediator collisionMediator) {
        this.context = context;
        this.collisionMediator = collisionMediator;
    }

    public void addObjects(String obj, int textureId, Square... squares) {
        ShadingProgram program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
        Model model = FundamentalGenerator.getModel(context, program, textureId, obj);

        for (Square s : squares) {
            collisionMediator.addObject(s);

            Node node = new Node();
            node.setModel(model);
            node.getRelativeTransform().setPosition(s.getPos().getX(), s.getPos().getY(), s.getPos().getZ());
            rootNode.getSonNodes().add(node);
        }
    }

    public void addObjects(String obj, int textureId, Circle... circles) {
        ShadingProgram program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
        Model model = FundamentalGenerator.getModel(context, program, textureId, obj);

        for (Circle c : circles) {
            collisionMediator.addObject(c);

            Node node = new Node();
            node.setModel(model);
            node.getRelativeTransform().setPosition(c.getPos().getX(), c.getPos().getY(), c.getPos().getZ());
            rootNode.getSonNodes().add(node);
        }
    }

    public void draw() {
        rootNode.updateTree(new SFTransform3f());
        rootNode.draw();
    }

}
