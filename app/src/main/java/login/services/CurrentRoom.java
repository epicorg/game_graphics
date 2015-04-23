package login.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import game.Team;
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
            int maxPlayers = json.getInt(FieldsNames.ROOM_MAX_PLAYERS);

            ArrayList<Team> teams = new ArrayList<>();
            JSONArray jTeams = json.getJSONArray(FieldsNames.ROOM_TEAM);
            for (int i = 0; i < jTeams.length(); i++) {
                JSONObject jTeam = jTeams.getJSONObject(i);
                String name = jTeam.getString(FieldsNames.NAME);
                int color = jTeam.getInt(FieldsNames.ROOM_TEAM_COLOR);

                Team team = new Team(name, color);

                ArrayList<Player> players = new ArrayList<>();
                JSONArray jPlayers = jTeam.getJSONArray(FieldsNames.LIST);
                for (int j = 0; j < jPlayers.length(); j++) {
                    JSONObject jPlayer = jPlayers.getJSONObject(j);
                    String playerName = jPlayer.getString(FieldsNames.NAME);

                    players.add(new Player(playerName));
                }

                team.setPlayers(players);
                teams.add(team);
            }
            currentRoomResult = new CurrentRoomResult(maxPlayers, teams);
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
        private ArrayList<Team> teams;

        public CurrentRoomResult(int maxPlayer, ArrayList<Team> teams) {
            this.maxPlayer = maxPlayer;
            this.teams = teams;
        }

        public int getMaxPlayer() {
            return maxPlayer;
        }

        public ArrayList<Team> getTeams() {
            return teams;
        }
    }

}
