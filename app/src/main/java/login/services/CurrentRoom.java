package login.services;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import login.interaction.FieldsNames;

/**
 * created by Luca on 31/03/2015
 */

public class CurrentRoom implements Service {

    private JSONObject json;
    private Handler handler;

    public CurrentRoom(JSONObject json) {
        super();
        this.json = json;
    }

    @Override
    public void start() {
        readFields();
    }

    private void readFields() {
        try {
            JSONObject object = json.getJSONObject(FieldsNames.ROOMS_LIST);
            CurrentRoomResult[] roomResults = new CurrentRoomResult[object.length()];
            Iterator<String> iterator = object.keys();

            int count = 0;
            while (iterator.hasNext()) {
                String name = iterator.next();
                json.getJSONObject(name);

                roomResults[count++] = new CurrentRoomResult(name);
            }

            Message message = handler.obtainMessage(0, roomResults);
            message.sendToTarget();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public class CurrentRoomResult {

        private String name;

        public CurrentRoomResult(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}
