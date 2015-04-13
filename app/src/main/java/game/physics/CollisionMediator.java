package game.physics;


import java.util.LinkedList;
import java.util.List;

public class CollisionMediator {

    public static final String LOG_TAG = "CollisionMediator";

    private List<CollisionBox> list=new LinkedList<>();

    public void addObject(CollisionBox c){
        list.add(c);
    }

    public CollisionBox collide(CollisionBox box){
        for (CollisionBox b : list) {
            if (b==box)
                continue;
            if (CollisionUtils.checkCollision(box, b))
                return b;
        }
        return null;
    }

    public void clear(){
        list.clear();
    }

}
