package login.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import login.interaction.FieldsNames;

/**
 * created by Luca on 31/03/2015
 */

public class Login implements Service {

    private JSONObject json;
    private Handler handler;

    public Login(JSONObject json) {
        super();
        this.json = json;
    }

    @Override
    public void start() {
        readFields();
    }

    private void readFields() {
        try {
            boolean value = json.getBoolean(FieldsNames.NO_ERRORS);
            LoginResult result;
            if (value) {
                int hashcode = json.getInt(FieldsNames.HASHCODE);
                result = new LoginResult(true, hashcode);

            } else {
                result = new LoginResult(false, 0);
            }
            Message message = handler.obtainMessage(0, result);
            message.sendToTarget();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class LoginResult {

        private boolean ok;
        private int hashcode;

        public LoginResult(boolean ok, int hashcode) {
            this.ok = ok;
            this.hashcode = hashcode;
        }

        public boolean isOk() {
            return ok;
        }

        public int getHashcode() {
            return hashcode;
        }
    }
}
