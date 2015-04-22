package com.example.alessandro.computergraphicsexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import game.GameManager;
import game.Room;
import game.player.Player;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;
import login.services.CurrentRoom;

/**
 * Created by Andrea on 18/04/2015.
 */
public class RoomActivity extends ActionBarActivity {

    public static final String LOG_TAG = "RoomActivity";

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private TextView roomStatus;
    private ListView playersList;
    private ArrayAdapter<Player> adapter;

    private Room currentRoom;

    private String username, roomName;
    private int hashcode;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        context = this;

        Intent intent = getIntent();
        username = intent.getStringExtra(FieldsNames.USERNAME);
        roomName = intent.getStringExtra(FieldsNames.ROOM_NAME);
        hashcode = intent.getIntExtra(FieldsNames.HASHCODE, 0);

        roomStatus = (TextView) findViewById(R.id.room_status);
        playersList = (ListView) findViewById(R.id.room_list);

        serverCommunicationThread.setHandler(new RoomHandler());
        try {
            serverCommunicationThread.send(createPlayerListRequest());
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        getSupportActionBar().setTitle(roomName);
    }

    private JSONObject createPlayerListRequest() {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.ROOMS);
            request.put(FieldsNames.SERVICE_TYPE, FieldsNames.ROOM_PLAYER_LIST);
            request.put(FieldsNames.HASHCODE, hashcode);
            request.put(FieldsNames.USERNAME, username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }


    public class RoomHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CurrentRoom.LIST:
                    processListMessage(msg);
                    break;
                case CurrentRoom.START:
                    processStartMessage(msg);
                    break;
            }
        }

        private void processListMessage(Message msg){
            CurrentRoom.CurrentRoomResult results = (CurrentRoom.CurrentRoomResult) msg.obj;
            currentRoom = new Room(roomName, results.getMaxPlayer(), results.getPlayers());

            adapter = new ArrayAdapter<Player>(getApplicationContext(), android.R.layout.simple_list_item_1, currentRoom.getPlayers());
            playersList.setAdapter(adapter);
        }

        private void processStartMessage(Message msg){
            boolean result = (boolean) msg.obj;

            if(result){
                GameManager gameManager = GameManager.getInstance();
                gameManager.setRoom(currentRoom);

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        }

    }
}
