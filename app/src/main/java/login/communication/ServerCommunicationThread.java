package login.communication;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import login.services.Service;


/**
 * Classe di gestione scambio dati con il server
 * <p/>
 * Metododo di utilizzo:
 * All'avvio dell'applicazione va avviato il thread recuperandone l'istanza con l'apposito metodo e
 * chiamando il metodo run()
 * e ogni volta che cambio activity devo settare il context!!!
 * <p/>
 * Created by Luca on 31/03/2015.
 */
public class ServerCommunicationThread extends Thread {

    public static final String LOG_TAG = "ServerCommunicationT";

    public static final String SERVER_ADDRESS = "192.168.1.2";
    public static final int SERVER_PORT = 7007;

    private static ServerCommunicationThread instance = new ServerCommunicationThread();

    private Handler handler;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ServiceChooser serviceChooser = new ServiceChooser();

    private boolean threadState;
    private ArrayList<ServerCommunicationThreadListener> threadListeners;

    private ServerCommunicationThread() {
        super();

        threadState = false;
        threadListeners = new ArrayList<>();
    }

    @Override
    public void run() {
        threadState = init();
        for (ServerCommunicationThreadListener l : threadListeners) {
            l.onThreadStateChanged(threadState);
        }
        if (!threadState) {
            return;
        }

        String line;
        JSONObject received;
        while (true) {
            try {
                line = reader.readLine();
                if (line != null) {
                    received = new JSONObject(line);
                    Log.d("RESPONSE", line);

                    Service service = serviceChooser.setService(received);
                    service.setHandler(handler);
                    service.start();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean init() {
        try {
            socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Init failed");
            return false;
        }
        return true;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void send(JSONObject object) {
        Log.d(LOG_TAG, "send: " + object.toString());
        writer.println(object.toString());
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("EXCEPTION", ex.toString());
        }
        return null;
    }

    public static ServerCommunicationThread getInstance() {
        return instance;
    }

    public void addServerCommunicationThreadListener(ServerCommunicationThreadListener l) {
        threadListeners.add(l);
    }

    public void removeServerCommunicationThreadListener(ServerCommunicationThreadListener l) {
        threadListeners.remove(l);
    }

}