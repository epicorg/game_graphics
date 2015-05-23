package com.example.alessandro.computergraphicsexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import game.JSONd;
import game.RequestMaker;
import game.Room;
import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.interaction.FieldsNames;
import login.interaction.RoomsErrorStrings;
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
//    private Activity activity = this;
    private String result;
    private RequestMaker requestMaker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        ImageButton fabImageButton = (ImageButton) findViewById(R.id.action_new_rom);

        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewRoomDialog();
            }
        });


        context = this;

        Intent intent = getIntent();
        username = intent.getStringExtra(FieldsNames.USERNAME);
        hashcode = intent.getIntExtra(FieldsNames.HASHCODE, 0);
        requestMaker=new RequestMaker(new JSONd(FieldsNames.USERNAME, username),
                new JSONd(FieldsNames.HASHCODE, hashcode));


        roomsList = (ListView) findViewById(R.id.rooms_list);
        roomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE, FieldsNames.ROOMS),
                            new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.ROOM_JOIN),
                            new JSONd(FieldsNames.ROOM_NAME, rooms.get(position).getName())));
                } catch (NotConnectedException e) {
                    Toast.makeText(context, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        getSupportActionBar().setTitle(R.string.rooms_title);
    }

    @Override
    protected void onResume() {
        super.onResume();

        serverCommunicationThread.setHandler(new RoomsHandler());

        try {
            serverCommunicationThread.send(createListRequest());
        } catch (NotConnectedException e) {
            Toast.makeText(this, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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
                showNewRoomDialog();
                return true;
            case R.id.action_refresh:
                try {
                    serverCommunicationThread.send(createListRequest());
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.logout_confirm)).setTitle(getString(R.string.logout));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createLogoutRequest();
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();


    }

    private void createLogoutRequest() {
        JSONObject logoutRequest = requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE, FieldsNames.LOGOUT));

        try {
            serverCommunicationThread.send(logoutRequest);
        } catch (NotConnectedException e) {
            Toast.makeText(context, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void showNewRoomDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Create new room");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_room, null);
        b.setView(dialogView);

        final EditText newRoomName = (EditText) dialogView.findViewById(R.id.rooms_new_room_name);

        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                result = newRoomName.getText().toString();

                try {
                    serverCommunicationThread.send(requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE, FieldsNames.ROOMS),
                            new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.ROOM_CREATE),
                            new JSONd(FieldsNames.ROOM_NAME, result)));
                } catch (NotConnectedException e) {
                    Toast.makeText(context, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });
        b.setNegativeButton("CANCEL", null);
        b.create().show();
    }

    private JSONObject createListRequest(){
        return requestMaker.getNewRequestWithDefaultRequests(new JSONd(FieldsNames.SERVICE, FieldsNames.ROOMS),
            new JSONd(FieldsNames.SERVICE_TYPE, FieldsNames.ROOMS_LIST));
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
                case Rooms.ERRORS:
                    processErrorMessage(msg);
                    break;
            }
        }

        private void processListMessage(Message msg) {
            rooms.clear();

            Rooms.RoomsResult[] results = (Rooms.RoomsResult[]) msg.obj;
            for (Rooms.RoomsResult r : results) {
                rooms.add(new Room(r.getName(), r.getMaxPlayers(), r.getCurrentPlayers()));
            }

            adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, rooms);
            roomsList.setAdapter(adapter);
        }

        private void processJoinMessage(Message msg) {
            Rooms.RoomJoinResult roomJoinResult = (Rooms.RoomJoinResult) msg.obj;
            if (roomJoinResult.getResult()) {
                Intent intent = new Intent(context, RoomActivity.class);
                intent.putExtra(FieldsNames.USERNAME, username);
                intent.putExtra(FieldsNames.HASHCODE, hashcode);
                intent.putExtra(FieldsNames.ROOM_NAME, roomJoinResult.getName());
                startActivity(intent);
            } else {
                Toast.makeText(context, getString(R.string.rooms_join_error), Toast.LENGTH_LONG).show();
            }
        }

        private void processErrorMessage(Message msg) {
            RoomsErrorStrings roomsErrorStrings = new RoomsErrorStrings();

            String errors = "";
            String[] results = (String[]) msg.obj;
            for (String s : results) {
                errors += getString(roomsErrorStrings.getStringIdByError(s)) + "\n";
            }

            Toast.makeText(context, errors, Toast.LENGTH_LONG).show();
        }

    }

}

