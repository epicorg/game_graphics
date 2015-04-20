package com.example.alessandro.computergraphicsexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import login.communication.NotConnectedException;
import login.communication.ServerCommunicationThread;
import login.communication.ServerCommunicationThreadListener;
import login.data.LoginData;
import login.interaction.FieldsNames;
import login.interaction.ProgressShower;
import login.services.Login;

/**
 * Activity di LogIn in cui l'utente inserisce l'username e la password per
 * essere riconosciuto dal server
 */
public class MainActivity extends ActionBarActivity implements ServerCommunicationThreadListener {

    private MainActivity thisActivity = this;
    private ServerCommunicationThread serverCommunicationThread;
    private HashMap<Integer, View> views = new HashMap<Integer, View>();
    private SharedPreferences loginPreference;
    private ProgressShower progressShower;
    private LoginData loginData;

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getViews();

        loginPreference = getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        progressShower = new ProgressShower(views.get(R.id.login_progress), views.get(R.id.login_form), getResources().getInteger(android.R.integer.config_shortAnimTime));

        //DEBUG
        views.get(R.id.game_graphics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        checkRememberMe();

        serverCommunicationThread = ServerCommunicationThread.getInstance();
        serverCommunicationThread.addServerCommunicationThreadListener(this);
        serverCommunicationThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        serverCommunicationThread.setHandler(new LoginHandler());
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            serverCommunicationThread.exit();
            super.onBackPressed();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.login_exit_message), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void checkRememberMe() {
        if (loginPreference.getBoolean("Remember", false)) {
            ((TextView) views.get(R.id.username)).setText(loginPreference.getString(FieldsNames.USERNAME, "user"));
            ((TextView) views.get(R.id.password)).setText(loginPreference.getString(FieldsNames.PASSWORD, "pass"));
            ((CheckBox) views.get(R.id.remeberMeBox)).setChecked(true);
        }
    }

    /**
     * Invocato per attivare/disattivare la funzione RememberMe
     */
    public void rememberMe(View view) {
        CheckBox rememberBox = (CheckBox) findViewById(R.id.remeberMeBox);
        SharedPreferences.Editor editor = loginPreference.edit();
        if (rememberBox.isChecked())
            editor.putBoolean("Remember", true);
        else
            editor.putBoolean("Remember", false);
        editor.commit();
    }

    /**
     * Passa all'Activity di registrazione, invocato dalll'apposto bottone
     */
    public void notRegistered(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * Avvia il Login inviando la richiesta al server
     */
    public void attemptLogin(View view) {
        ((TextView) views.get(R.id.username)).setError(null);
        ((TextView) views.get(R.id.password)).setError(null);
        loginData = getData();
        boolean cancel = loginData.checkData(getApplicationContext(), views);

        if (!cancel) {
            progressShower.showProgress(true);
            try {
                serverCommunicationThread.send(createRequest());
            } catch (NotConnectedException e) {
                progressShower.showProgress(false);
                Toast.makeText(thisActivity, getString(R.string.error_not_connected), Toast.LENGTH_SHORT).show();
                //showAlertDialog(getString(R.string.error_not_connected));
                e.printStackTrace();
            }
        }
    }

    private JSONObject createRequest() {
        JSONObject request = new JSONObject();
        try {
            request.put(FieldsNames.SERVICE, FieldsNames.LOGIN);
            request.put(FieldsNames.USERNAME, loginData.getUsername());
            request.put(FieldsNames.PASSWORD, loginData.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    // recupera i dati dai campi di testo
    private LoginData getData() {
        String username = ((TextView) views.get(R.id.username)).getText().toString();
        String password = ((TextView) views.get(R.id.password)).getText().toString();
        return new LoginData(username, password);
    }

    @Override
    public void onThreadStateChanged(final boolean threadState) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (threadState) {
                    ((TextView) views.get(R.id.status)).setText("Connected");
                    findViewById(R.id.log_in).setEnabled(true);
                } else {
                    ((TextView) views.get(R.id.status)).setText("Not Connected");
                    findViewById(R.id.log_in).setEnabled(false);
                }
            }
        });
    }

    /**
     * Handler che permette la comunicazione tra il Thread di login attivato alla ricezione della risposta dal server e
     * l'Activity di login stessa ricevendo i risultati del Login
     */
    public class LoginHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Login.LoginResult result = (Login.LoginResult) msg.obj;
            if (result.isOk()) {
                if (loginPreference.getBoolean("Remember", false)) {
                    SharedPreferences.Editor editor = loginPreference.edit();
                    editor.putString(FieldsNames.USERNAME, loginData.getUsername());
                    editor.putString(FieldsNames.PASSWORD, loginData.getPassword());
                    editor.commit();
                    Log.d("REMEMBER", "fields saved");
                }
                Intent intent = new Intent(thisActivity, RoomsActivity.class);
                intent.putExtra(FieldsNames.USERNAME, loginData.getUsername());
                intent.putExtra(FieldsNames.HASHCODE, result.getHashcode());
                thisActivity.startActivity(intent);
            } else {
                showAlertDialog(getString(R.string.login_failed));
            }
            progressShower.showProgress(false);
            Log.d("RESULT", String.valueOf(result.isOk()));
        }

    }

    private void showAlertDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
        builder.setMessage(error).setTitle(getString(R.string.dialog_error));
        builder.setPositiveButton(getString(R.string.dialog_try_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void getViews() {
        views.put(R.id.status, findViewById(R.id.status));
        views.put(R.id.username, findViewById(R.id.username));
        views.put(R.id.password, findViewById(R.id.password));
        views.put(R.id.remeberMeBox, findViewById(R.id.remeberMeBox));
        views.put(R.id.login_form, findViewById(R.id.login_form));
        views.put(R.id.login_progress, findViewById(R.id.login_progress));

        views.put(R.id.game_graphics, findViewById(R.id.game_graphics)); //DEBUG
    }

}