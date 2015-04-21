package login.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import game.player.Player;
import login.interaction.FieldsNames;

/**
 * created by Luca on 31/03/2015
 */

public class CurrentRoom implements Service {

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
            JSONArray jsonPlayers = json.getJSONArray(FieldsNames.ROOM_PLAYER_LIST);
            int maxPlayers = json.getInt(FieldsNames.ROOM_PLAYER_LIST);

            ArrayList<Player> players = new ArrayList<>();
            for(int i = 0; i < jsonPlayers.length(); i++){
                players.add(new Player(null, jsonPlayers.getString(i)));
            }

            CurrentRoomResult currentRoomResult = new CurrentRoomResult(maxPlayers, players);
            Message message = handler.obtainMessage(0, currentRoomResult);
            message.sendToTarget();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
