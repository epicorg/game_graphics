package com.example.alessandro.computergraphicsexample;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alessandro.computergraphicsexample.R;

import login.call.CallManager;
import login.communication.ServerCommunicationThread;


public class CallActivity extends ActionBarActivity {

    private boolean _doubleBackToExitPressedOnce = false;
    private CallManager callManager;
    private ServerCommunicationThread serverCommunicationThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        callManager = CallManager.getInstance();
        serverCommunicationThread = ServerCommunicationThread.getInstance();
        serverCommunicationThread.setHandler(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (_doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        this._doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.info_press_again_to_quit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                _doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void call(View view){

        String username = ((EditText) findViewById(R.id.user_call) ).getText().toString();

        /**try {
            callManager.makeCall(username);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            new CustomAlertDialog(getString(R.string.dialog_error)
                    ,"impossibile chiamare", getString(R.string.dialog_try_again),
                    this).show();
            e.printStackTrace();
        }*/

        AudioManager Audio =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Audio.setMode(AudioManager.MODE_IN_CALL);
        Audio.setSpeakerphoneOn(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
