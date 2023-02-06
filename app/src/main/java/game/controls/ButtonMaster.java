package game.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import graphic.integration.Model;
import graphic.integration.Node;
import graphic.shadow.math.SFMatrix3f;
import graphic.shadow.math.SFTransform3f;
import graphic.shadow.math.SFVertex3f;

/**
 * It keeps a list of {@link Button} and draws them.
 *
 * @author De Pace
 * @see Button
 */
public class ButtonMaster {

    private final HashMap<Button, Node> map = new HashMap<>();
    private Model model;
    private final ArrayList<Node> parentNodes;

    /**
     * Creates a new <code>ButtonMaster</code>.
     */
    public ButtonMaster() {
        parentNodes = new ArrayList<>();
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Adds a <code>Button</code>, representing it with the last set Model. The <code>Button</code>
     * is represented with a <code>Node</code> placed and rotated compared with the parentNode. Is
     * useful to give a common pre-transformation to more <code>Button</code>.
     *
     * @param button     <code>Button</code> to be added
     * @param position   <code>Button</code> position relative to the parentNode
     * @param angle      <code>Button</code> rotation angle relative to the parentNode
     * @param parentNode <code>Node</code> which can contain transformations for more Button
     */
    public void addButton(Button button, SFVertex3f position, float angle, Node parentNode) {
        Node node = new Node();
        map.put(button, setupNode(node, position, model, angle));

        Node realParent;
        if (parentNodes.contains(parentNode)) {
            realParent = parentNodes.get(parentNodes.indexOf(parentNode));
        } else {
            realParent = parentNode;
            parentNodes.add(realParent);
        }
        realParent.getSonNodes().add(node);
    }

    /**
     * @return associated <code>Button</code> set
     */
    public Set<Button> getButtons() {
        return map.keySet();
    }

    /**
     * @param button <code>Button</code> which <code>Node</code> representation is requested
     * @return node which represents the specified <code>Button</code>
     */
    public Node getButtonNode(Button button) {
        return map.get(button);
    }

    /**
     * Draws every <code>Button</code>.
     */
    public void draw() {
        for (Node n : parentNodes) {
            n.updateTree(new SFTransform3f());
            n.draw();
        }
    }

    private Node setupNode(Node node, SFVertex3f position, Model model, float angleZ) {
        SFMatrix3f tmp = SFMatrix3f.getRotationZ(angleZ);
        node.setModel(model);
        node.getRelativeTransform().setPosition(position);
        node.getRelativeTransform().setMatrix(tmp);
        return node;
    }

}
