package game.graphics;

import game.player.Player;

import static android.opengl.Matrix.frustumM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.setLookAtM;

/**
 * Created by depa on 06/04/15.
 */
public class Camera {

    private final float[] orthoMatrix=new float[16],
            resultMatrix=new float[16];
    private Player me;

    public Camera(Player player){
        this.me=player;
    }

    public void setMatrices(int width, int height, float ratio) {
        setResultMatrix(ratio);
        setOrthoMatrix(width, height, ratio);
    }

    private void setOrthoMatrix(int width, int height, float ratio) {
        if (width > height) {
            orthoM(orthoMatrix, 0, -ratio, ratio, -1, 1, -1, 1);
        } else {
            orthoM(orthoMatrix, 0, -1, 1, -(1 / ratio), (1 / ratio), -1, 1);
        }
    }

    private void setResultMatrix(float ratio) {
        final float[] viewMatrix = new float[16];
        final float[] projectionMatrix = new float[16];
        setViewMatrix(viewMatrix);
        if (ratio>1)
            frustumM(projectionMatrix, 0, -1, 1, -1/ratio, 1/ratio, 1, 64);
        else
            frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 64);
        multiplyMM(resultMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    private void setViewMatrix(float[] viewMatrix) {
        float eyeX, eyeY, eyeZ, centerX, centerY, centerZ;
        eyeX = me.getStatus().getPosition().getX();
        eyeY = me.getStatus().getPosition().getY();
        eyeZ = me.getStatus().getPosition().getZ();
        centerX = me.getStatus().getPosition().getX() + me.getStatus().getDirection().getX();
        centerY = me.getStatus().getPosition().getY() + me.getStatus().getDirection().getY();
        centerZ = me.getStatus().getPosition().getZ() + me.getStatus().getDirection().getZ();

        setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
    }

    public float[] getOrthoMatrix(){
        return orthoMatrix;
    }

    public float[] getResultMatrix(){
        return resultMatrix;
    }


}
