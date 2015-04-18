package game.network;

import java.net.InetSocketAddress;
import java.util.ArrayList;

/**
 * Created by Andrea on 18/04/2015.
 */
public class Server {

    public static final String LOG_TAG = "Server";

    private String name;
    private InetSocketAddress address;

    public InetSocketAddress getAddress() {
        return address;
    }

    public ArrayList<Room> getRoomsList() {
        //TODO

        ArrayList<Room> rooms = new ArrayList<>();
        return rooms;
    }

}
