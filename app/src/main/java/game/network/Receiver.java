package game.network;

import java.net.Socket;

/**
 * Created by Andrea on 18/04/2015.
 */
public class Receiver {

    private Socket socket;

    public Receiver(Socket socket) {
        this.socket = socket;
    }
}
