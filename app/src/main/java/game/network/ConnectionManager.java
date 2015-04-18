package game.network;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Andrea on 18/04/2015.
 */
public class ConnectionManager {

    public static final String LOG_TAG = "ConnectionManager";

    private Socket socket;
    private Receiver receiver;
    private Sender sender;

    public void connect(Server s) {
        try {
            socket = new Socket(s.getAddress().getHostName(), s.getAddress().getPort());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Cannot create socket.");
        }

        Log.d(LOG_TAG, "Socket created.");

        receiver = new Receiver(socket);
        sender = new Sender(socket);
    }

    public void disconnect(Server s) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Cannot close socket.");
        }

        Log.d(LOG_TAG, "Socket closed.");
    }


}
