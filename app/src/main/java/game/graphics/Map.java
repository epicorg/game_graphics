package game.graphics;

import java.util.HashMap;
import java.util.Set;
import game.physics.CollisionBox;
import game.physics.CollisionMediator;

public class Map {

    private HashMap<CollisionBox[],GraphicObject> map=new HashMap<>();

    public void addObjects(String obj, int textureId, CollisionBox... boxes) {
        map.put(boxes,new GraphicObject(textureId,obj));
    }

    public void loadMapLogic(CollisionMediator cm){
        for (CollisionBox[] boxes: map.keySet()){
            for (CollisionBox c: boxes){
                    cm.addObject(c);
            }
        }
    }

    public Set<CollisionBox[]> getBoxes(){
        return map.keySet();
    }

    public GraphicObject getBoxesGraphics(CollisionBox[] boxes){
        return map.get(boxes);
    }

}
