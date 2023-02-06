package graphic.integration;

import java.util.ArrayList;

import graphic.shadow.math.SFMatrix3f;
import graphic.shadow.math.SFTransform3f;
import graphic.shadow.math.SFVertex3f;

public class Node {

    protected SFTransform3f relativeTransform;
    protected SFTransform3f effectiveTransform;
    private ArrayList<Node> sonNodes;
    private boolean enabled;
    private Model model;

    public Node() {
        setup();
    }


    public Node(SFTransform3f relativeTransform) {
        setup();
        this.relativeTransform = relativeTransform;
    }

    public Node(SFTransform3f relativeTransform, Model model) {
        setup();
        this.relativeTransform = relativeTransform;
        this.model = model;
    }

    public void setup() {
        relativeTransform = new SFTransform3f();
        effectiveTransform = new SFTransform3f();
        sonNodes = new ArrayList<Node>();
        model = null;
        enabled = true;
    }

    public Node cloneNode() {
        SFTransform3f transform = new SFTransform3f();
        transform.set(getRelativeTransform());
        Node node = new Node(transform, getModel());
        for (int i = 0; i < getSonNodes().size(); i++) {
            node.getSonNodes().add(getSonNodes().get(i).cloneNode());
        }
        return node;
    }

    public float getX() {
        return this.effectiveTransform.getV()[9];
    }

    public float getY() {
        return this.effectiveTransform.getV()[10];
    }

    public float getZ() {
        return this.effectiveTransform.getV()[11];
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ArrayList<Node> getSonNodes() {
        return sonNodes;
    }

    public SFTransform3f getRelativeTransform() {
        return relativeTransform;
    }

    public void updateTransform(SFTransform3f fatherTransform) {

        SFVertex3f position = new SFVertex3f();
        SFMatrix3f matrix = new SFMatrix3f();
        SFMatrix3f fatherMatrix = new SFMatrix3f();
        relativeTransform.getPosition(position);
        relativeTransform.getMatrix(matrix);
        fatherTransform.getMatrix(fatherMatrix);
        /* M' = Fm * M */
        matrix = fatherMatrix.multMatrix(matrix);
        /* M' = Fm * Q + Fq */
        fatherTransform.transform(position);
        effectiveTransform.setPosition(position);
        effectiveTransform.setMatrix(matrix);
    }

    public void updateTree(SFTransform3f fatherTransform) {
        this.updateTransform(fatherTransform);
        if (enabled) {
            for (int i = 0; i < sonNodes.size(); i++) {
                sonNodes.get(i).updateTree(effectiveTransform);
            }
        }
    }

    public void getOpenGLMatrix(float[] matrix) {

        SFMatrix3f m = new SFMatrix3f();
        SFVertex3f vertex = new SFVertex3f();
        effectiveTransform.getPosition(vertex);
        effectiveTransform.getMatrix(m);

        matrix[0] = m.getA();
        matrix[1] = m.getD();
        matrix[2] = m.getG();
        matrix[3] = 0;

        matrix[4] = m.getB();
        matrix[5] = m.getE();
        matrix[6] = m.getH();
        matrix[7] = 0;

        matrix[8] = m.getC();
        matrix[9] = m.getF();
        matrix[10] = m.getI();
        matrix[11] = 0;

        matrix[12] = vertex.getX();
        matrix[13] = vertex.getY();
        matrix[14] = vertex.getZ();
        matrix[15] = 1;
    }

    public void draw() {
        if (enabled) {
            if (model != null) {
                float[] matrix = new float[16];
                getOpenGLMatrix(matrix);
                //glUniformMatrix4fv(shaderModel.getUniformTransform(), 1, false, matrix);
                model.getProgram().setTransform(matrix);
                model.draw();
            }
            for (int i = 0; i < sonNodes.size(); i++) {
                sonNodes.get(i).draw();
            }
        }
    }
}
