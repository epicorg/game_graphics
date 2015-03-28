package generators;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

import com.example.alessandro.computergraphicsexample.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import sfogl.integration.Material;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLSystemState;
import shadow.math.SFMatrix3f;

/**
 * Created by Andrea on 27/03/2015.
 */
public class ButtonsGenerator {

    public static final String LOG_TAG = "ButtonsGenerator";

    public static final int LEFT_TEXTURE = R.drawable.color_red;
    public static final int RIGHT_TEXTURE = R.drawable.color_green;
    public static final int UP_TEXTURE = R.drawable.color_blue;
    public static final int DOWN_TEXTURE = R.drawable.color_white;
    public static final int LEFT_COLOR = Color.argb(0, 255, 0, 0);
    public static final int RIGHT_COLOR = Color.argb(0, 0, 255, 0);
    public static final int UP_COLOR = Color.argb(0, 0, 0, 255);
    public static final int DOWN_COLOR = Color.argb(0, 255, 255, 255);

    private Model model;
    private int width, height;

    private Node leftNode = new Node();
    private Node rightNode = new Node();
    private Node upNode = new Node();
    private Node downNode = new Node();

    private Material leftMaterial, rightMaterial, upMaterial, downMaterial;

    private ArrayList<Node> buttonsNodes = new ArrayList<>();

    public ButtonsGenerator(Context context, ShadingProgram program, Model model, int width, int height) {
        this.model = model;
        this.width = width;
        this.height = height;

        setup(context, program);
    }

    private void setup(Context context, ShadingProgram program) {
        leftMaterial = FundamentalGenerator.getMaterial(context, program, LEFT_TEXTURE);
        rightMaterial = FundamentalGenerator.getMaterial(context, program, RIGHT_TEXTURE);
        upMaterial = FundamentalGenerator.getMaterial(context, program, UP_TEXTURE);
        downMaterial = FundamentalGenerator.getMaterial(context, program, DOWN_TEXTURE);

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

        buttonsNodes.add(mainNode);
    }

    public ArrayList<Node> getButtons() {
        return buttonsNodes;
    }

    public boolean isInsideAButton(float touchX, float touchY, int height) {
        boolean result = getColorAt(touchX, touchY, height) != 0;

        Log.d(LOG_TAG, "It's " + (result ? "inside " : "not inside ") + "a control.");

        return result;
    }

    public void startColorPicking(float touchX, float touchY, int height) {
        pickButtonAndCallListener(getColorAt(touchX, touchY, height));
    }

    private int getColorAt(float touchX, float touchY, int height) {
        SFOGLSystemState.cleanupColorAndDepth(0, 0, 0, 1);
        drawForColorPicking();

        ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(4);
        pixelBuffer.order(ByteOrder.nativeOrder());
        pixelBuffer.position(0);
        GLES20.glReadPixels((int) touchX, (int) (height - touchY), 1, 1, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
        byte b[] = new byte[4];
        pixelBuffer.get(b);
        int R = 0xff & b[0];
        int G = 0xff & b[1];
        int B = 0xff & b[2];

        return Color.argb(0, R, G, B);
    }

    private void drawForColorPicking() {
        savePreviousMaterialDrawRestore(leftNode, leftMaterial);
        savePreviousMaterialDrawRestore(rightNode, rightMaterial);
        savePreviousMaterialDrawRestore(upNode, upMaterial);
        savePreviousMaterialDrawRestore(downNode, downMaterial);
    }

    private void savePreviousMaterialDrawRestore(Node node, Material material) {
        Material tmpMaterial = node.getModel().getMaterialComponent();
        node.getModel().setMaterialComponent(material);
        node.draw();
        node.getModel().setMaterialComponent(tmpMaterial);
    }

    private void pickButtonAndCallListener(int color) {
        if (color == LEFT_COLOR) {
            Log.d(LOG_TAG, "Pressed LEFT.");
        } else if (color == RIGHT_COLOR) {
            Log.d(LOG_TAG, "Pressed RIGHT.");
        } else if (color == UP_COLOR) {
            Log.d(LOG_TAG, "Pressed UP.");
        } else if (color == DOWN_COLOR) {
            Log.d(LOG_TAG, "Pressed DOWN.");
        }
    }

}
