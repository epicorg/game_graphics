package game.graphics;

import game.player.Player;

import static android.opengl.Matrix.frustumM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.setLookAtM;

/**
 * Oggetto telecamera, si occupa di gestire le matrici per la proiezione 3D e 2D, seguendo la posizione di un Player.
 * @author Stefano De Pace
 *
 */
public class Camera {

    private final float[] orthoMatrix=new float[16],
            resultMatrix=new float[16];
    private float[] projectionMatrix=new float[16];
    private Player me;
    private float znear,zfar,k;

    /**
     * Crea un nuovo oggetto telecamera che guarda dalla posizione di un Player, in base alla sua direzione.
     * @param player il Player da seguire,
     * @param znear minima distanza di un oggetto dalla telecamera per essere visualizzato
     *              nella proiezione 3D.
     * @param zfar massima distanza di un oggetto dalla telecamera per essere visualizzato
     *              nella proiezione 3D.
     * @param angle angolo di visione della proiezione 3D tra alto-basso e destra-sinistra in gradi.
     */
    public Camera(Player player, float znear, float zfar, float angle){
        this.me=player;
        this.znear=znear;
        this.zfar=zfar;
        this.k=znear*(float)Math.tan(angle*Math.PI/360);
    }

    /**
     * Aggiorna le matrici di proiezione 3D e 2D, da chiamare se le dimensioni dello schermo cambiano.
     * @param ratio rapporto larghezza/altezza dello schermo.
     */
    public void updateMatrices(float ratio){
        setProjection(ratio);
        setOrthoMatrix(ratio);
    }

    /**
     * Restituisce la matrice di proiezione 2D.
     * @return la matrice di proiezione 2D.
     */
    public float[] getOrthoMatrix(){
        return orthoMatrix;
    }

    /**
     * Restituisce la matrice di proiezione 3D, ricalcolata sulla pozione corrente del Player.
     * @return matrice di proiezione 3D corrente.
     */
    public float[] getResultMatrix(){
        setResultMatrix();
        return resultMatrix;
    }

    private void setOrthoMatrix(float ratio) {
        if (ratio>1)
            orthoM(orthoMatrix, 0, -ratio, ratio, -1, 1, -1, 1);
        else
            orthoM(orthoMatrix, 0, -1, 1, -(1 / ratio), (1 / ratio), -1, 1);
    }

    private void setProjection(float ratio) {
        if (ratio>1)
            frustumM(projectionMatrix, 0, -k, k, -(k/ratio), (k/ratio), znear, zfar);
        else
            frustumM(projectionMatrix, 0, -k*ratio, k*ratio, -k, k, znear, zfar);
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
