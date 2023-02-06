package game.net.communication;

import static game.net.communication.ServerCommunicationThreadState.CONNECTED;
import static game.net.communication.ServerCommunicationThreadState.CONNECTING;
import static game.net.communication.ServerCommunicationThreadState.ENCRYPTING;
import static game.net.communication.ServerCommunicationThreadState.NOT_CONNECTED;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import epic.org.R;
import game.net.connection_encryption.ConnectionEncrypter;
import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.RegisterFields;
import game.net.fieldsnames.ServicesFields;
import game.net.services.Service;

/**
 * Manages exchange of data between client and server.
 * <p/>
 * How to use: at the application launch, the thread has to be started getting the instance with the
 * appropriated method and calling the run() method. Every time the activity changes, the context
 * must be set.
 *
 * @author Micieli
 * @date 31/03/2015
 * @see Handler
 */
public class ServerCommunicationThread extends Thread {

    private static final String LOG_TAG = "ServerCommunication";

    public static final int WAIT_TIME = 5000;

    public static final int SERVER_PORT = 7007;
    private static String serverAddress;
    private static ServerCommunicationThread instance;
    private int waitTime;
    private int serverPort;
    private ArrayList<ServerCommunicationThreadListener> threadListeners;
    private ServerCommunicationThreadState threadState;

    private Handler handler;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private final ServiceChooser serviceChooser = new ServiceChooser();

    private ServerCommunicationThread() {
    }

    /**
     * @return the IP address of the first <code>InetAddress</code> of the first <code>NetworkInterface</code>.
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return the unique instance of the <code>ServerCommunicationThread</code>.
     */
    public static ServerCommunicationThread getInstance() {
        if (instance == null) {
            instance = new ServerCommunicationThread();
            instance.threadState = NOT_CONNECTED;
            instance.serverPort = SERVER_PORT;
            instance.waitTime = WAIT_TIME;
            instance.threadListeners = new ArrayList<>();
        }
        return instance;
    }

    /**
     * @return the actual address of the server to connect.
     */
    public static String getServerAddress() {
        return serverAddress;
    }

    /**
     * Sets the address of the server to connect.
     *
     * @param address the address of the server to connect.
     */
    public static void setServerAddress(String address) {
        serverAddress = address;
    }

    /**
     * Initializes the <code>ServerCommunicationThread</code> with custom parameters.
     *
     * @param waitTime   time to wait while trying to connect to the server.
     * @param serverPort port of the server to connect to.
     */
    public void init(int waitTime, int serverPort) {
        this.waitTime = waitTime;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {

        init();

        if (threadState != CONNECTED)
            return;

        encrypt();

        String line;
        JSONObject received;
        while (true) {
            try {
                line = reader.readLine();
                if (line != null) {
                    received = new JSONObject(ConnectionEncrypter.decryptResponse(line));
                    Service service = serviceChooser.setService(received);
                    service.setHandler(handler);
                    service.start(received);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                setStateAndUpdate(NOT_CONNECTED);
                return;
            }
        }
    }

    private void encrypt() {
        EncryptInitializer initializer = new EncryptInitializer();
        initializer.initConnection();
        Log.d(LOG_TAG, "Start encryption");
        setStateAndUpdate(ENCRYPTING);
    }

    private void init() {

        setStateAndUpdate(CONNECTING);

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(InetAddress.getByName(serverAddress), serverPort), waitTime);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            setStateAndUpdate(CONNECTED);
            Log.d(LOG_TAG, "Initialization successful");
        } catch (IOException e) {
            e.printStackTrace();
            setStateAndUpdate(NOT_CONNECTED);
            Log.d(LOG_TAG, "Initialization failed");
        }
    }

    /**
     * Closes the connection and eliminates the reference to the instance of the <code>ServerCommunicationThread</code>.
     */
    public void exit() {
        try {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setStateAndUpdate(NOT_CONNECTED);

        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        instance = null;
        Log.e(LOG_TAG, "Exit successful");
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void send(JSONObject object) throws NotConnectedException {
        if (writer == null || (threadState != CONNECTED && threadState != ENCRYPTING))
            throw new NotConnectedException();
        new Thread(() -> {
            try {
                if (object.getString(ServicesFields.SERVICE.toString()).equals(ServicesFields.ENCRYPT.toString()))
                    writer.println(object);
                else
                    writer.println(ConnectionEncrypter.encryptRequest(object.toString()));
            } catch (JSONException e) {
                writer.println(object);
            }
        }).start();
    }

    public void setStateAndUpdate(ServerCommunicationThreadState state) {
        threadState = state;
        for (ServerCommunicationThreadListener l : threadListeners)
            l.onThreadStateChanged(threadState);
    }

    public ServerCommunicationThreadState getThreadState() {
        return threadState;
    }

    public void addServerCommunicationThreadListener(ServerCommunicationThreadListener l) {
        threadListeners.add(l);
    }

    public void removeServerCommunicationThreadListener(ServerCommunicationThreadListener l) {
        threadListeners.remove(l);
    }

}
