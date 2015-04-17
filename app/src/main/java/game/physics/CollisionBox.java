package game.physics;

import java.util.ArrayList;
import shadow.math.SFVertex3f;

/**
 * Interfaccia per generiche scatole di collisione 2D, per un sistema basato sul SAT
 * (Separating Axis Theorem).
 *@author Stefano De Pace
 *
 */
public interface CollisionBox {

    /**
     * Restituisce la posizione del centro della CollisionBox.
     * @return SFVertex3f rappresentante il centro della CollisionBox.
     */
    public SFVertex3f getPos();

    /**
     * Restituisce il raggio della CollisionBox, usato per un checking rapido.
     * @return raggio del cerchio circoscritto alla forma geometrica della CollisionBox.
     */
    public float getRadius();

    /**
     * Restituisce una lista di vettori normalizzati, rappresentanti gli assi ortogonali ai lati, da passare a getProjections.
     * @param otherCenter centro di un'altra CollisionBox rispetto a cui calcolare la collisione (è importante solo per forme curve).
     * @return lista di vettori normalizzati ortogonali ai lati.
     */
    public ArrayList<SFVertex3f> getAxes(SFVertex3f otherCenter);

    /**
     * Restituisce l'intervallo delle proiezioni della forma lungo l'asse dato.
     * @param ax Vettore normalizzato lungo la cui direzione calcolare le proiezioni.
     * @return restituisce un array le cui due componenti rappresentano l'estremo inferiore e l'estremo superiore delle proiezioni della figura lungo l'asse dato.
     */
    public float[] getProjections(SFVertex3f ax);

}
