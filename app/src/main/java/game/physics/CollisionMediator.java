package game.physics;

import java.util.LinkedList;
import java.util.List;

public class CollisionMediator {

    private List<Collidable> list = new LinkedList<>();

    public void addObject(Collidable c) {
        list.add(c);
    }

    public Collidable collide(Collidable col) {
        for (Collidable c : list) {
            if (c == col)
                continue;
            if (col.getBox().checkCollision(c.getBox()))
                return c;
        }
        return null;
    }

    public Collidable collide(CollisionBox box) {
        for (Collidable c : list) {
            if (c.getBox() == box)
                continue;
            if (box.checkCollision(c.getBox()))
                return c;
        }
        return null;
    }

    public List<Collidable> collideList(CollisionBox box) {
        LinkedList<Collidable> list2 = new LinkedList<>();
        for (Collidable c : list) {
            if (c.getBox() == box)
                continue;
            if (box.checkCollision(c.getBox()))
                list2.add(c);
        }
        return list2;
    }

    public void clear() {
        list.clear();
    }

}
