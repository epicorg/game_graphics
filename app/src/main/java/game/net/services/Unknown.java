package game.net.services;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import game.net.fieldsnames.ServicesFields;

/**
 * Represents an unknown <code>Service</code>.
 *
 * @author Noris
 * @date 26/03/2015
 */
public class Unknown implements Service {

    @Override
    public void start(JSONObject json) {
    }

    @Override
    public void setHandler(Handler handler) {
    }

    private JSONObject getResponse() {
        JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse.put(ServicesFields.SERVICE.toString(), ServicesFields.UNKNOWN.toString());
            return jsonResponse;
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

}
