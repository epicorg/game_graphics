package game.network;

import java.net.Socket;

/**
 * Created by Andrea on 18/04/2015.
 */
public class Sender {

    private Socket socket;

    public Sender(Socket socket) {
        this.socket = socket;
    }
}
