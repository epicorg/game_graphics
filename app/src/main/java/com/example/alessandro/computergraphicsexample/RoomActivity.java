package com.example.alessandro.computergraphicsexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import game.player.Player;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;
import login.services.CurrentRoom;

/**
 * Created by Andrea on 18/04/2015.
 */
public class RoomActivity extends Activity {

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private ListView playersList;
    private ArrayAdapter<Player> adapter;
    private ArrayList<Player> players = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_room);

        playersList = (ListView) findViewById(R.id.room_list);
        serverCommunicationThread.setHandler(new RoomHandler());
        try {
            serverCommunicationThread.send(createPlayerListRequest());
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private JSONObject createPlayerListRequest() {
        JSONObject request = new JSONObject();
        Intent intent =getIntent();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.ROOMS);
            request.put(FieldsNames.HASHCODE, intent.getIntExtra(FieldsNames.HASHCODE,0));
            request.put(FieldsNames.USERNAME, intent.getStringExtra(FieldsNames.USERNAME));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }


    public class RoomHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            CurrentRoom.CurrentRoomResult[] results = (CurrentRoom.CurrentRoomResult[]) msg.obj;
            for (CurrentRoom.CurrentRoomResult r : results) {
                players.add(new Player(null, r.getName()));
            }

            adapter = new ArrayAdapter<Player>(getApplicationContext(), android.R.layout.simple_list_item_1, players);
            playersList.setAdapter(adapter);
        }
    }
}
