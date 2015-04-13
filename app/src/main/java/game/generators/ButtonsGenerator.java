package game.generators;

import java.util.ArrayList;
import java.util.List;

import sfogl.integration.Model;
import sfogl.integration.Node;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

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

        List<Node> list=mainNode.getSonNodes();
        list.add(setupNode(leftNode, new SFVertex3f(-2, 0, 0), model, (float) -Math.PI / 2));
        list.add(setupNode(rightNode, new SFVertex3f(2, 0, 0), model, (float) Math.PI / 2));
        list.add(setupNode(upNode, new SFVertex3f(0, 2, 0), model, 0));
        list.add(setupNode(downNode, new SFVertex3f(0, -2, 0), model, (float) Math.PI));

        mainNode.updateTree(new SFTransform3f());

        buttonsNodes.add(mainNode);
    }

    public Node setupNode(Node node, SFVertex3f position, Model model, float angleZ){
        SFMatrix3f tmp = SFMatrix3f.getRotationZ(angleZ);
        node.setModel(model);
        node.getRelativeTransform().setPosition(position);
        node.getRelativeTransform().setMatrix(tmp);
        return node;
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
