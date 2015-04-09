package game.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import game.generators.FundamentalGenerator;
import sfogl.integration.Material;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLSystemState;
import shadow.math.SFTransform3f;

/**
 * Created by Andrea on 27/03/2015.
 */
public class ButtonsControl {

    public static final String LOG_TAG = "ButtonsControl";

    public static final int LEFT_COLOR = Color.argb(255, 1, 0, 0);
    public static final int RIGHT_COLOR = Color.argb(255, 2, 0, 0);
    public static final int UP_COLOR = Color.argb(255, 3, 0, 0);
    public static final int DOWN_COLOR = Color.argb(255, 4, 0, 0);

    private Context context;
    private ShadingProgram program;
    private float[] orthoMatrix;
    private Node leftNode;
    private Node rightNode;
    private Node upNode;
    private Node downNode;
    private Material leftMaterial, rightMaterial, upMaterial, downMaterial;

    private Bitmap buttonsBitmap;

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
        leftMaterial = FundamentalGenerator.getColorMaterial(context, program, LEFT_COLOR);
        rightMaterial = FundamentalGenerator.getColorMaterial(context, program, RIGHT_COLOR);
        upMaterial = FundamentalGenerator.getColorMaterial(context, program, UP_COLOR);
        downMaterial = FundamentalGenerator.getColorMaterial(context, program, DOWN_COLOR);
    }

    public void update(int width, int height) {
        program.setupProjection(orthoMatrix);
        SFOGLSystemState.cleanupColorAndDepth(0, 0, 0, 1);
        drawForColorPicking();

        int size = width * height;
        ByteBuffer buffer = ByteBuffer.allocateDirect(size * 4);
        buffer.order(ByteOrder.nativeOrder());
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
        int pixelsBuffer[] = new int[size];
        buffer.asIntBuffer().get(pixelsBuffer);
        buffer = null;

        for (int i = 0; i < size; ++i) {
            pixelsBuffer[i] = ((pixelsBuffer[i] & 0xff00ff00)) | ((pixelsBuffer[i] & 0x000000ff) << 16) | ((pixelsBuffer[i] & 0x00ff0000) >> 16);
        }

        buttonsBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        buttonsBitmap.setPixels(pixelsBuffer, size - width, -width, 0, 0, width, height);
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

    public boolean isInsideAButton(float touchX, float touchY) {
        boolean result = getColorAt(touchX, touchY) != Color.argb(255, 0, 0, 0);
        Log.d(LOG_TAG, "It's " + (result ? "inside " : "not inside ") + "a control.");

        return result;
    }

    public ButtonPositions getPressedButton(float touchX, float touchY) {
        program.setupProjection(orthoMatrix);
        return getButtonPosition(getColorAt(touchX, touchY));
    }

    private int getColorAt(float touchX, float touchY) {
        return buttonsBitmap.getPixel((int) touchX, (int) touchY);
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
