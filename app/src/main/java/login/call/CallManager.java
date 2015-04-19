package login.call;

import android.net.rtp.AudioStream;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import login.call.audio.AudioCallManager;
import login.communication.ServerCommunicationThread;


/**
 * Created by Luca on 29/03/2015.
 */
public class CallManager {

    public static final String SERVER_ADDRESS = "192.168.1.4";
    public static final int PORT = 7007;

    private static CallManager instance = new CallManager();
    private AudioCallManager audioCallManager = AudioCallManager.getInstance();
    private ServerCommunicationThread serverCommunicationThread;
    private HashMap<String, Call> streams = new HashMap<String, Call>();

    private CallManager() {
        serverCommunicationThread = ServerCommunicationThread.getInstance();
    }

    public static CallManager getInstance() {
        return instance;
    }

    public void makeCall(String callee, String caller) throws SocketException, UnknownHostException {

        AudioStream stream = audioCallManager.newAudioStream();

        JSONObject callRequest = new JSONObject();
        try {
            callRequest.put("service", "Call");
            callRequest.put("callee", callee);
            callRequest.put("caller", caller);
            callRequest.put("port", stream.getLocalPort());
            Log.d("CallRequest", callRequest.toString());

            //serverCommunicationThread.send(callRequest);
            //TODO
            /**if(jsonObject1.getBoolean("value")){
             stream.associate(InetAddress.getByName(jsonObject1.getString("ip"))
             ,jsonObject1.getInt("port"));

             }*/


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
