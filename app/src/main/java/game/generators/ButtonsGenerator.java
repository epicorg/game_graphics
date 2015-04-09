package game.generators;

import java.util.ArrayList;

import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;

/**
 * Created by Andrea on 27/03/2015.
 */
public class ButtonsGenerator {

    public static final String LOG_TAG = "ButtonsGenerator";

    private Model model;

    private Node leftNode = new Node();
    private Node rightNode = new Node();
    private Node upNode = new Node();
    private Node downNode = new Node();

    private ArrayList<Node> buttonsNodes = new ArrayList<>();

    public ButtonsGenerator(Model model) {
        this.model = model;

        setup();
    }

    private void setup() {
        float scaling = 0.15f;

        Node mainNode = new Node();
        mainNode.getRelativeTransform().setPosition(-0.50f, -0.50f, 1);
        mainNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(scaling, scaling, scaling));

        leftNode.setModel(model);
        rightNode.setModel(model);
        upNode.setModel(model);
        downNode.setModel(model);

        SFMatrix3f tmp = SFMatrix3f.getRotationZ((float) -Math.PI / 2);
        leftNode.getRelativeTransform().setPosition(-2, 0, 0);
        leftNode.getRelativeTransform().setMatrix(tmp);

        tmp = SFMatrix3f.getRotationZ((float) Math.PI / 2);
        rightNode.getRelativeTransform().setPosition(2, 0, 0);
        rightNode.getRelativeTransform().setMatrix(tmp);

        upNode.getRelativeTransform().setPosition(0, 2, 0);

        tmp = SFMatrix3f.getRotationZ((float) Math.PI);
        downNode.getRelativeTransform().setPosition(0, -2, 0);
        downNode.getRelativeTransform().setMatrix(tmp);

        mainNode.getSonNodes().add(leftNode);
        mainNode.getSonNodes().add(rightNode);
        mainNode.getSonNodes().add(upNode);
        mainNode.getSonNodes().add(downNode);

        mainNode.updateTree(new SFTransform3f());

        buttonsNodes.add(mainNode);
    }

    public ArrayList<Node> getButtons() {
        return buttonsNodes;
    }

    public Node getDownNode() {
        return downNode;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public Node getUpNode() {
        return upNode;
    }

}
