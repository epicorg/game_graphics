package game.musics;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.alessandro.computergraphicsexample.R;

/**
 * Created by Andrea on 17/04/2015.
 */
public class GameSoundtracks {

    public static final String LOG_TAG = "GameSoundtracks";

    public static Uri[] getSoundtracks(Context context) {
        Uri[] uris = new Uri[]
                {
                        Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.soundtrack_01),
                        Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.soundtrack_02)
                };

        return uris;
    }

}
