package game.net.services;

import android.os.Handler;

import org.json.JSONObject;

import game.data.UserData;
import game.net.communication.JSONd;
import game.net.communication.NotConnectedException;
import game.net.communication.RequestMaker;
import game.net.communication.ServerCommunicationThread;
import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.ServicesFields;

/**
 * Polling service respond to Server request, in order to confirm that I am alive.
 *
 * @author Micieli
 */
public class Polling implements Service {

    @Override
    public void start(JSONObject json) {
        try {
            ServerCommunicationThread.getInstance().send(generatePollingResponse());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private JSONObject generatePollingResponse() {
        RequestMaker maker = UserData.DATA.getRequestMakerWithData(CommonFields.USERNAME, CommonFields.HASHCODE);
        return maker.getNewRequestWithDefaultRequests(new JSONd(ServicesFields.SERVICE, ServicesFields.POLLING));
    }

    @Override
    public void setHandler(Handler handler) {
    }

}
