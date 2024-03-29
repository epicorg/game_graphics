package game.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

/**
 * @author Micieli
 * @date 13/05/2015
 */
public class HeadsetListener extends BroadcastReceiver {

    private final Context context;

    public HeadsetListener(Context context) {
        this.context = context;
    }

    public void init() {
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        context.registerReceiver(this, receiverFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra("state", 0);
        AudioManager Audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Audio.setSpeakerphoneOn(state == 0);
    }

    public void release() {
        context.unregisterReceiver(this);
    }

}
