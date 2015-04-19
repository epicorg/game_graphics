package com.example.alessandro.computergraphicsexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import game.network.Room;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;
import login.services.Rooms;

/**
 * Created by Andrea on 18/04/2015.
 */
public class RoomsActivity extends ActionBarActivity {

    public static final String LOG_TAG = "RoomsActivity";

    private ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private ListView roomsList;
    private ArrayAdapter<Room> adapter;
    private ArrayList<Room> rooms = new ArrayList<>();

    private String username;
    private int hashcode;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        context = this;

        Intent intent = getIntent();
        username = intent.getStringExtra(FieldsNames.USERNAME);
        hashcode = intent.getIntExtra(FieldsNames.HASHCODE, 0);

        roomsList = (ListView) findViewById(R.id.rooms_list);
        roomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                serverCommunicationThread.send(createJoinRoomRequest(rooms.get(position)));
            }
        });

        serverCommunicationThread.setHandler(new RoomsHandler());
        serverCommunicationThread.send(createListRequest());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rooms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_new_rom:
                serverCommunicationThread.send(createNewRoomRequest());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private JSONObject createNewRoomRequest() {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.ROOMS);
            request.put(FieldsNames.SERVICE_TYPE, FieldsNames.CREATE_ROOM);
            request.put(FieldsNames.ROOM_NAME, "NomePerTest");
            request.put(FieldsNames.HASHCODE, hashcode);
            request.put(FieldsNames.USERNAME, username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    private JSONObject createJoinRoomRequest(Room room) {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.ROOMS);
            request.put(FieldsNames.SERVICE_TYPE, FieldsNames.ROOM_JOIN);
            request.put(FieldsNames.HASHCODE, hashcode);
            request.put(FieldsNames.USERNAME, username);
            request.put(FieldsNames.ROOM_NAME, room.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    private JSONObject createListRequest() {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.ROOMS);
            request.put(FieldsNames.SERVICE_TYPE, FieldsNames.ROOMS_LIST);
            request.put(FieldsNames.HASHCODE, hashcode);
            request.put(FieldsNames.USERNAME, username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    public class RoomsHandler extends android.os.Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Rooms.LIST:
                    processListMessage(msg);
                    break;
                case Rooms.JOIN:
                    processJoinMessage(msg);
                    break;
            }
        }

        private void processJoinMessage(Message msg) {
            if ((boolean) msg.obj) {
                Intent intent = new Intent(context, RoomActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
            }
        }

        private void processListMessage(Message msg) {
            rooms.clear();

            Rooms.RoomsResult[] results = (Rooms.RoomsResult[]) msg.obj;
            for (Rooms.RoomsResult r : results) {
                rooms.add(new Room(r.getName(), r.getMaxPlayers(), r.getCurrentPlayers()));
            }

            adapter = new ArrayAdapter<Room>(context, android.R.layout.simple_list_item_1, rooms);
            roomsList.setAdapter(adapter);
        }

    }

}
