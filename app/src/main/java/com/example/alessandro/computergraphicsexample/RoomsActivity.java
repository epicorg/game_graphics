package com.example.alessandro.computergraphicsexample;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        roomsList = (ListView) findViewById(R.id.rooms_list);
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
            request.put(FieldsNames.SERVICE_TYPE, FieldsNames.ROOMS_CREATE);
            request.put(FieldsNames.ROOM_NAME, "NomePerTest");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }


    public class RoomsHandler extends android.os.Handler {

        @Override
        public void handleMessage(Message msg) {
            rooms.clear();
            
            Rooms.RoomsResult[] results = (Rooms.RoomsResult[]) msg.obj;
            Log.d(LOG_TAG, "Number of rooms: " + results.length);
            for (Rooms.RoomsResult r : results) {
                rooms.add(new Room(r.getName(), r.getMaxPlayers(), r.getCurrentPlayers()));
            }

            adapter = new ArrayAdapter<Room>(getApplicationContext(), android.R.layout.simple_list_item_1, rooms);
            roomsList.setAdapter(adapter);
        }

    }


}
