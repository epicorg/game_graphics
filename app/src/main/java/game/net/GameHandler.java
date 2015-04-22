package game.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

import game.codes.TextureCodes;
import game.graphics.Map;
import game.graphics.Obstacle;
import game.graphics.Wall;
import game.physics.Circle;
import game.physics.Square;
import login.services.Game;
import shadow.math.SFVertex3f;

/**
 * Created by Andrea on 22/04/2015.
 */
public class GameHandler extends Handler {

    public static final String LOG_TAG = "GameHandler";

    private ArrayList<GameHandlerListener> gameHandlerListeners = new ArrayList<>();

    private Map map;

    private int groundWidth = 0;
    private int groundHeight = 0;

    public void addGameHandlerListeners(GameHandlerListener l) {
        gameHandlerListeners.add(l);
    }

    public void removeGameHandlerListeners(GameHandlerListener l) {
        gameHandlerListeners.remove(l);
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d(LOG_TAG, "handleMessage");
        switch (msg.what) {
            case Game.STATUS:
                break;
            case Game.MAP:
                processMapMessage(msg);
                break;
            case Game.POSITIONS:
                break;
        }
    }

    private void processMapMessage(Message msg) {
        Log.d(LOG_TAG, "processMapMessage");
        Game.GameMapResult results = (Game.GameMapResult) msg.obj;
        map = new Map();

        for (Game.GameMapObject o : results.getGameMapObjects()) {
            if (o.object.equals("Wall")) {
                float posX = Float.parseFloat(o.position.split(" ")[0]);
                float posY = Float.parseFloat(o.position.split(" ")[1]);
                float posZ = Float.parseFloat(o.position.split(" ")[2]);
                float sizeX = Float.parseFloat(o.size.split(" ")[0]);
                float sizeY = Float.parseFloat(o.size.split(" ")[1]);
                float sizeZ = Float.parseFloat(o.size.split(" ")[2]);
                int texture = TextureCodes.getTextureIdFromString(o.texture);
                map.addObjects(new Wall(new Square(new SFVertex3f(posX, posY, posZ), sizeX, sizeY, sizeZ), texture));
            } else if (o.object.equals("Obstacle")) {
                float posX = Float.parseFloat(o.position.split(" ")[0]);
                float posY = Float.parseFloat(o.position.split(" ")[1]);
                float posZ = Float.parseFloat(o.position.split(" ")[2]);
                float sizeX = Float.parseFloat(o.size.split(" ")[0]);
                float sizeY = Float.parseFloat(o.size.split(" ")[1]);
                int texture = TextureCodes.getTextureIdFromString(o.texture);
                map.addObjects(new Obstacle(new Circle(new SFVertex3f(posX, posY, posZ), sizeX), sizeY, texture));
            }
        }

        groundWidth = results.getWidth();
        groundHeight = results.getHeight();

        for (GameHandlerListener l : gameHandlerListeners)
            l.onMapReceived();
    }

    public Map getMap() {
        return map;
    }

    public int getGroundWidth() {
        return groundWidth;
    }

    public int getGroundHeight() {
        return groundHeight;
    }
}