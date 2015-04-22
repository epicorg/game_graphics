package login.services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import login.interaction.FieldsNames;

public class Game implements Service {

    public static final String LOG_TAG = "Game";

    public static final int STATUS = 0;
    public static final int MAP = 1;
    public static final int POSITIONS = 2;

    private JSONObject json;
    private Handler handler;

    public Game(JSONObject json) {
        super();
        this.json = json;
    }

    @Override
    public void start() {
        readFields();
    }

    private void readFields() {
        try {
            Message message = null;
            switch (json.getString(FieldsNames.SERVICE_TYPE)) {
                case FieldsNames.GAME_STATUS:
                    message = getGameStatusMessage();
                    break;
                case FieldsNames.GAME_MAP:
                    message = getGameMapMessage();
                    break;
                case FieldsNames.GAME_POSITIONS:
                    message = getGamePositionsMessage();
                    break;
            }
            message.sendToTarget();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private Message getGameStatusMessage() {
        GameStatusResult gameStatusResult = null;
        boolean go = false;
        try {
            go = json.getBoolean(FieldsNames.GAME_GO);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gameStatusResult = new GameStatusResult(go);
        return handler.obtainMessage(STATUS, gameStatusResult);
    }

    private Message getGameMapMessage() {
        GameMapResult gameMapResult = null;
        int width = 0;
        int height = 0;
        ArrayList<GameMapObject> gameMapObjects = new ArrayList<>();
        try {
            JSONArray jItems = json.getJSONArray(FieldsNames.GAME_ITEMS);
            for (int i = 0; i < jItems.length(); i++) {
                JSONObject jObject = jItems.getJSONObject(i);
                String object = jObject.getString(FieldsNames.GAME_OBJECT);
                String texture = jObject.getString(FieldsNames.GAME_TEXTURE);
                String position = jObject.getString(FieldsNames.GAME_POSITION);
                String size = jObject.getString(FieldsNames.GAME_SIZE);
                gameMapObjects.add(new GameMapObject(object, texture, position, size));
            }

            width = json.getInt(FieldsNames.GAME_WIDTH);
            height = json.getInt(FieldsNames.GAME_HEIGHT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gameMapResult = new GameMapResult(width, height, gameMapObjects);
        return handler.obtainMessage(MAP, gameMapResult);
    }

    private Message getGamePositionsMessage() {
        GamePositionsResult gamePositionsResult = null;
        ArrayList<GamePositionsObject> gamePositionsObjects = new ArrayList<>();
        try {
            JSONArray jItems = json.getJSONArray(FieldsNames.GAME_ITEMS);
            for (int i = 0; i < jItems.length(); i++) {
                JSONObject jObject = jItems.getJSONObject(i);
                String name = jObject.getString(FieldsNames.GAME_PLAYER_NAME);
                String position = jObject.getString(FieldsNames.GAME_POSITION);
                gamePositionsObjects.add(new GamePositionsObject(name, position));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        gamePositionsResult = new GamePositionsResult(gamePositionsObjects);
        return handler.obtainMessage(POSITIONS, gamePositionsResult);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class GameStatusResult {

        private boolean go;

        public GameStatusResult(boolean go) {
            this.go = go;
        }

        public boolean isGo() {
            return go;
        }

    }

    public class GameMapResult {

        private int width, height;
        private ArrayList<GameMapObject> gameMapObjects;

        public GameMapResult(int width, int height, ArrayList<GameMapObject> gameMapObjects) {
            this.width = width;
            this.height = height;
            this.gameMapObjects = gameMapObjects;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public ArrayList<GameMapObject> getGameMapObjects() {
            return gameMapObjects;
        }

    }

    public class GameMapObject {
        public String object, texture, position, size;

        public GameMapObject(String object, String texture, String position, String size) {
            this.object = object;
            this.texture = texture;
            this.position = position;
            this.size = size;
        }
    }

    public class GamePositionsResult {

        private ArrayList<GamePositionsObject> gamePositionsObjects;

        public GamePositionsResult(ArrayList<GamePositionsObject> gamePositionsObjects) {
            this.gamePositionsObjects = gamePositionsObjects;
        }

        public ArrayList<GamePositionsObject> getGamePositionsObjects() {
            return gamePositionsObjects;
        }

    }

    public class GamePositionsObject {
        public String name, position;

        public GamePositionsObject(String name, String position) {
            this.name = name;
            this.position = position;
        }

    }

}
