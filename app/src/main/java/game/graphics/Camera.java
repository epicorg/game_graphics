package game.graphics;

import static android.opengl.Matrix.frustumM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.setLookAtM;

import game.player.Player;

/**
 * Video camera: it manages the matrices for 3D and 2D projections following the position of a {@link Player}.
 *
 * @author De Pace
 */
public class Camera {

    private final float[] orthogonalMatrix = new float[16],
            resultMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final Player me;
    private final float zNear, zFar, k;

    /**
     * Creates a new camera object from a <code>Player</code> position, on the strength of its direction.
     *
     * @param player The <code>Player</code> to be followed.
     * @param zNear  Minimum distance between an object and the camera
     *               to have the object displayed in the 3D projection.
     * @param zFar   Maximum distance between an object and the camera
     *               to have the object displayed in the 3D projection.
     * @param angle  Vision angle of the 3D projection between up-down and right-left, expressed in degrees.
     */
    public Camera(Player player, float zNear, float zFar, float angle) {
        this.me = player;
        this.zNear = zNear;
        this.zFar = zFar;
        this.k = zNear * (float) Math.tan(angle * Math.PI / 360);
    }

    /**
     * Updated the matrices of the 3D and 2D projection.
     * Has to be invoked if screen dimension changes.
     *
     * @param ratio Screen width/height ratio.
     */
    public void updateMatrices(float ratio) {
        setProjection(ratio);
        setOrthogonalMatrix(ratio);
    }

    /**
     * @return 2D projection matrix.
     */
    public float[] getOrthogonalMatrix() {
        return orthogonalMatrix;
    }

    /**
     * @return Current 3D projection matrix.
     */
    public float[] getResultMatrix() {
        setResultMatrix();
        return resultMatrix;
    }

    private void setOrthogonalMatrix(float ratio) {
        if (ratio > 1)
            orthoM(orthogonalMatrix, 0, -ratio, ratio, -1, 1, -1, 1);
        else
            orthoM(orthogonalMatrix, 0, -1, 1, -(1 / ratio), (1 / ratio), -1, 1);
    }

    private void setProjection(float ratio) {
        if (ratio > 1)
            frustumM(projectionMatrix, 0, -k, k, -(k / ratio), (k / ratio), zNear, zFar);
        else
            frustumM(projectionMatrix, 0, -k * ratio, k * ratio, -k, k, zNear, zFar);
    }

    private void setResultMatrix() {
        final float[] viewMatrix = new float[16];
        setViewMatrix(viewMatrix);
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

}
