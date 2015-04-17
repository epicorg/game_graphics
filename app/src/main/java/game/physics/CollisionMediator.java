package game.physics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import shadow.math.SFVertex3f;

/**
 * Mediatore che gestisce il sistema di collisioni, memorizzando tutti gli oggetti che devono essere controllati,
 * e implementando il sistema SAT.
 * @author Stefano De Pace
 *
 */
public class CollisionMediator {

    public static final String LOG_TAG = "CollisionMediator";

    private List<CollisionBox> list=new LinkedList<>();

    /**
     * Aggiunge una CollisionBox all'elenco degli oggetti nel sistema di collision checking.
     * @param c CollisionBox da aggiungere all'elenco degli oggetti.
     */
    public void addObject(CollisionBox c){
        list.add(c);
    }

    /**
     * Controlla se una data CollisionBox fa collisione con una presente nel sistema.
     * @param box CollisionBox per cui cercare collisioni.
     * @return la prima CollisionBox nel sistema con cui si registra una collisione:
     * se non vi sono collisioni restituisce null.
     */
    public CollisionBox collide(CollisionBox box){
        for (CollisionBox b : list) {
            if (b==box)
                continue;
            if (checkCollision(box, b))
                return b;
        }
        return null;
    }

    /**
     * Controlla se vi è una collisione fra le due CollisionBox date; il risultato è indipendente
     * dall'ordine, tuttavia una forma curva potrebbe velocizzare il processo se assegnata per prima.
     * @param box1 prima CollisionBox di cui controllare la collisione.
     * @param box2 seconda CollisionBox di cui controllare la collisione.
     * @return restituisce true se registra collisione tra le due CollisionBox,
     * false altrimenti.
     */
    public static boolean checkCollision(CollisionBox box1, CollisionBox box2){
        SFVertex3f temp=new SFVertex3f(box2.getPos());
        float d=box1.getRadius()+box2.getRadius();
        temp.subtract(box1.getPos());
        return ((new SFVertex3f(temp.getX(),0,temp.getZ())).getSquareModulus()<=d*d
                && (checkOneBoxAxes(box1, box2) && checkOneBoxAxes(box2, box1)));
    }

    /**
     * Svuota la lista delle CollisionBox.
     */
    public void clear(){
        list.clear();
    }

    private static boolean checkOneBoxAxes(CollisionBox box1, CollisionBox box2){
        ArrayList<SFVertex3f> list=box1.getAxes(box2.getPos());
        for (SFVertex3f v : list) {
            if (!checkIntervals(box1.getProjections(v), box2.getProjections(v)))
                return false;
        }
        return true;
    }

    private static boolean checkIntervals(float[] i1, float[] i2){
        return (i1[1]-i2[0])*(i2[1]-i1[0])>0;
    }

}
