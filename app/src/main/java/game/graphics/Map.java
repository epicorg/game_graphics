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
import shadow.math.SFVertex3f;

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
            rootNode.getSonNodes().add(createNodeWithModel(model, s.getPos()));
        }
    }

    public void addObjects(String obj, int textureId, Circle... circles) {
        ShadingProgram program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);
        Model model = FundamentalGenerator.getModel(context, program, textureId, obj);

        for (Circle c : circles) {
            collisionMediator.addObject(c);
            rootNode.getSonNodes().add(createNodeWithModel(model, c.getPos()));
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
