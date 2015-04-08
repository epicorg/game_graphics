package game.controls;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

import com.example.alessandro.computergraphicsexample.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import game.generators.FundamentalGenerator;
import sfogl.integration.Material;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLSystemState;

/**
 * Created by Andrea on 27/03/2015.
 */
public class ButtonsControl {

    public static final String LOG_TAG = "ButtonsControl";

    public static final int LEFT_TEXTURE = R.drawable.color_red;
    public static final int RIGHT_TEXTURE = R.drawable.color_green;
    public static final int UP_TEXTURE = R.drawable.color_blue;
    public static final int DOWN_TEXTURE = R.drawable.color_white;
    public static final int LEFT_COLOR = Color.argb(0, 255, 0, 0);
    public static final int RIGHT_COLOR = Color.argb(0, 0, 255, 0);
    public static final int UP_COLOR = Color.argb(0, 0, 0, 255);
    public static final int DOWN_COLOR = Color.argb(0, 255, 255, 255);
    private Context context;
    private ShadingProgram program;
    private float[] orthoMatrix;
    private Node leftNode;
    private Node rightNode;
    private Node upNode;
    private Node downNode;
    private Material leftMaterial, rightMaterial, upMaterial, downMaterial;

    public ButtonsControl(Context context, ShadingProgram program, float[] orthoMatrix, Node leftNode, Node rightNode, Node upNode, Node downNode) {
        this.context = context;
        this.program = program;
        this.orthoMatrix = orthoMatrix;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.upNode = upNode;
        this.downNode = downNode;

        setup();
    }

    private void setup() {
        leftMaterial = FundamentalGenerator.getMaterial(context, program, LEFT_TEXTURE);
        rightMaterial = FundamentalGenerator.getMaterial(context, program, RIGHT_TEXTURE);
        upMaterial = FundamentalGenerator.getMaterial(context, program, UP_TEXTURE);
        downMaterial = FundamentalGenerator.getMaterial(context, program, DOWN_TEXTURE);
    }

    public boolean isInsideAButton(float touchX, float touchY) {
        boolean result = getColorAt(touchX, touchY) != 0;
        Log.d(LOG_TAG, "It's " + (result ? "inside " : "not inside ") + "a control.");

        return result;
    }

    public ButtonPositions getPressedButton(float touchX, float touchY) {
        program.setupProjection(orthoMatrix);
        return getButtonPosition(getColorAt(touchX, touchY));
    }

    private int getColorAt(float touchX, float touchY) {
        SFOGLSystemState.cleanupColorAndDepth(0, 0, 0, 1);
        drawForColorPicking();

        ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(4);
        pixelBuffer.order(ByteOrder.nativeOrder());
        pixelBuffer.position(0);
        GLES20.glReadPixels((int) touchX, (int) touchY, 1, 1, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
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

    private ButtonPositions getButtonPosition(int color) {
        if (color == LEFT_COLOR) {
            Log.d(LOG_TAG, "Pressed LEFT.");
            return ButtonPositions.LEFT;
        } else if (color == RIGHT_COLOR) {
            Log.d(LOG_TAG, "Pressed RIGHT.");
            return ButtonPositions.RIGHT;
        } else if (color == UP_COLOR) {
            Log.d(LOG_TAG, "Pressed UP.");
            return ButtonPositions.UP;
        } else if (color == DOWN_COLOR) {
            Log.d(LOG_TAG, "Pressed DOWN.");
            return ButtonPositions.DOWN;
        } else {
            return ButtonPositions.NULL;
        }
    }
}
