package login.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import game.player.Player;
import login.interaction.FieldsNames;

/**
 * created by Luca on 31/03/2015
 */

public class CurrentRoom implements Service {

    public static final int LIST = 0;
    public static final int START = 1;

    private JSONObject json;
    private Handler handler;

    public CurrentRoom(JSONObject json) {
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
                case FieldsNames.ROOM_PLAYER_LIST:
                    message = getPlayerListMessage();
                    break;
                case FieldsNames.ROOM_START:
                    //TODO
                    break;
            }
            message.sendToTarget();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private Message getPlayerListMessage() {
        CurrentRoomResult currentRoomResult = null;
        try {
            JSONArray jsonPlayers = json.getJSONArray(FieldsNames.LIST);
            int maxPlayers = json.getInt(FieldsNames.ROOM_MAX_PLAYERS);

            ArrayList<Player> players = new ArrayList<>();
            for (int i = 0; i < jsonPlayers.length(); i++) {
                players.add(new Player(null, jsonPlayers.getString(i)));
            }

            currentRoomResult = new CurrentRoomResult(maxPlayers, players);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return handler.obtainMessage(LIST, currentRoomResult);
    }

    private Message getStartMessage() throws JSONException {
        boolean result = json.getBoolean(FieldsNames.RESULT);

        return handler.obtainMessage(START, result);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class CurrentRoomResult {

        private int maxPlayer;
        private ArrayList<Player> players;

        public CurrentRoomResult(int maxPlayer, ArrayList<Player> players) {

            this.maxPlayer = maxPlayer;
            this.players = players;
        }

        public int getMaxPlayer() {
            return maxPlayer;
        }

        public ArrayList<Player> getPlayers() {
            return players;
        }

    }

}
