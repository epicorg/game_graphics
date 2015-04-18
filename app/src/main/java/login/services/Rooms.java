package login.services;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import login.interaction.FieldsNames;

/**
 * created by Luca on 31/03/2015
 */

public class Rooms implements Service {

    public static final String LOG_TAG = "Rooms";

    private JSONObject json;
    private Handler handler;

    public Rooms(JSONObject json) {
        super();
        this.json = json;
    }

    @Override
    public void start() {
        readFields();
    }

    private void readFields() {
        try {
            JSONObject object = json.getJSONObject(FieldsNames.ROOMS_LIST);
            RoomsResult[] roomsResults = new RoomsResult[object.length()];
            Iterator<String> iterator = object.keys();

            int count = 0;
            while (iterator.hasNext()) {
                String name = iterator.next();

                JSONObject curObj = object.getJSONObject(name);

                int maxPlayers = curObj.getInt(FieldsNames.ROOM_MAX_PLAYERS);
                int currentPlayers = curObj.getInt(FieldsNames.ROOM_CURRENT_PLAYERS);
                roomsResults[count++] = new RoomsResult(name, maxPlayers, currentPlayers);
            }

            Message message = handler.obtainMessage(0, roomsResults);
            message.sendToTarget();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class RoomsResult {

        private String name;
        private int maxPlayers, currentPlayers;

        public RoomsResult(String name, int maxPlayers, int currentPlayers) {
            this.name = name;
            this.maxPlayers = maxPlayers;
            this.currentPlayers = currentPlayers;
        }

        public String getName() {
            return name;
        }

        public int getMaxPlayers() {
            return maxPlayers;
        }

        public int getCurrentPlayers() {
            return currentPlayers;
        }
    }

}
