package game.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

import game.generators.FundamentalGenerator;
import sfogl.integration.Material;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLSystemState;

/**
 * Created by depa on 21/04/15.
 */
public class ButtonsControl {

    public static final String LOG_TAG = "ButtonsControl";

    private HashMap<Integer, Button> map=new HashMap<>();
    private HashMap<Button, Material> map2=new HashMap<>();

    private ShadingProgram program;
    private float[] orthoMatrix;

    private Bitmap buttonsBitmap;
    private ButtonMaster buttonMaster;

    public ButtonsControl(Context context, ShadingProgram program, float[] orthoMatrix, ButtonMaster buttonMaster) {
        this.program = program;
        this.orthoMatrix = orthoMatrix;
        this.buttonMaster=buttonMaster;
        int n=0;
        for(Button b: buttonMaster.getButtons()){
            n++;
            int color=generateNewColor(n);
            map.put(color, b);
            map2.put(b, FundamentalGenerator.getColorMaterial(context,program,color));
        }
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

    public boolean isInsideAButton(float touchX, float touchY) {
        return getColorAt(touchX, touchY) != Color.argb(255, 0, 0, 0);
    }

    public Button getPressedButton(float touchX, float touchY) {
        program.setupProjection(orthoMatrix);
        return map.get(getColorAt(touchX,touchY));
    }



    private int generateNewColor(int n){
        return Color.argb(255, n, 0, 0);
    }

    private void drawForColorPicking() {
        for(Button b: map2.keySet()){
            savePreviousMaterialDrawRestore(buttonMaster.getButtonNode(b), map2.get(b));
        }
    }

    private void savePreviousMaterialDrawRestore(Node node, Material material) {
        Material tmpMaterial = node.getModel().getMaterialComponent();
        node.getModel().setMaterialComponent(material);
        node.draw();
        node.getModel().setMaterialComponent(tmpMaterial);
    }

    private int getColorAt(float touchX, float touchY) {
        return buttonsBitmap.getPixel((int) touchX, (int) touchY);
    }

}
