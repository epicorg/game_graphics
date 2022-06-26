package epic.org;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Objects;

import game.net.communication.ConnectionStateMap;
import game.net.communication.JSONd;
import game.net.communication.NotConnectedException;
import game.net.communication.RequestMaker;
import game.net.communication.ServerCommunicationThread;
import game.net.communication.ServerCommunicationThreadListener;
import game.net.communication.ServerCommunicationThreadState;
import game.net.data.LoginData;
import game.net.data.OperationStarter;
import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.ServicesFields;
import game.net.handling.EncryptionHandler;
import game.net.handling.LoginHandler;
import game.net.handling.LoginHandlerListener;
import game.net.interaction.ProgressShower;

/**
 * Login activity: the user inserts username and password to be recognized by server.
 */
public class MainActivity extends AppCompatActivity implements ServerCommunicationThreadListener, LoginHandlerListener, OperationStarter {

    private static final String LOG_TAG = "MainActivity";

    private Activity activity;
    private ServerCommunicationThread serverCommunicationThread;
    private final HashMap<Integer, View> views = new HashMap<>();

    private ProgressShower progressShower;
    private SharedPreferences loginPreference;
    private LoginData loginData;

    private boolean doubleBackToExitPressedOnce;
    private boolean canLogin = false;
    private LoginHandler loginHandler;
    private EncryptionHandler encryptionHandler;
    private final ConnectionStateMap connectionStateMap = new ConnectionStateMap();

    private final RequestMaker requestMaker = new RequestMaker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        getViews();

        loginPreference = getSharedPreferences("LOGIN_PREF", Context.MODE_PRIVATE);
        progressShower = new ProgressShower(views.get(R.id.login_progress), views.get(R.id.login_form), getResources().getInteger(android.R.integer.config_shortAnimTime));

        Objects.requireNonNull(views.get(R.id.main_refresh)).setOnClickListener(v -> startThreadIfNotStarted());

        checkRememberMe();
        startThreadIfNotStarted();
    }

    private void startThreadIfNotStarted() {
        if (serverCommunicationThread != null)
            serverCommunicationThread.exit();

        canLogin = false;
        serverCommunicationThread = ServerCommunicationThread.getInstance();
        serverCommunicationThread.addServerCommunicationThreadListener(this);
        setThreadHandler();
        try {
            serverCommunicationThread.start();
        } catch (IllegalThreadStateException e) {
            e.printStackTrace();
            updateThreadState(serverCommunicationThread.getThreadState());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setThreadHandler();
    }

    private void setThreadHandler() {
        if (loginHandler == null || encryptionHandler == null) {
            loginHandler = new LoginHandler(this);
            loginHandler.addLoginHandlerListeners(this);
            encryptionHandler = new EncryptionHandler(this);
        }
        if (canLogin) {
            serverCommunicationThread.setHandler(loginHandler);
            Log.d(LOG_TAG, "Login Handler");
        } else {
            serverCommunicationThread.setHandler(encryptionHandler);
            Log.d(LOG_TAG, "Encryption Handler");
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.login_exit_message), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void checkRememberMe() {
        if (loginPreference.getBoolean("Remember", false)) {
            String username = loginPreference.getString(CommonFields.USERNAME.toString(), null);
            String password = loginPreference.getString(CommonFields.PASSWORD.toString(), null);
            if ((username != null) && (password != null)) {
                ((TextView) Objects.requireNonNull(views.get(R.id.username))).setText(username);
                ((TextView) Objects.requireNonNull(views.get(R.id.password))).setText(password);
                ((CheckBox) Objects.requireNonNull(views.get(R.id.rememberMeBox))).setChecked(true);
            } else {
                ((CheckBox) Objects.requireNonNull(views.get(R.id.rememberMeBox))).setChecked(false);
            }
        }
    }

    /**
     * Is called to activate/deactivate the RememberMe function.
     */
    public void rememberMe(View view) {
        CheckBox rememberBox = findViewById(R.id.rememberMeBox);
        SharedPreferences.Editor editor = loginPreference.edit();
        editor.putBoolean("Remember", rememberBox.isChecked());
        editor.apply();
    }

    private void saveRememberMeData() {
        if (loginPreference.getBoolean("Remember", false)) {
            SharedPreferences.Editor editor = loginPreference.edit();
            editor.putString(CommonFields.USERNAME.toString(), loginData.getUsername());
            editor.putString(CommonFields.PASSWORD.toString(), loginData.getPassword());
            editor.apply();
            Log.d(LOG_TAG, "Remember: Fields Saved");
        }
    }

    /**
     * Changes to the registration activity.
     */
    public void notRegistered(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }


    @Override
    public void start(Object parameter) {
        canLogin = true;
        serverCommunicationThread.setHandler(loginHandler);
        Log.d(LOG_TAG, "Can login!");
    }

    /**
     * Starts the login sending the request to the server.
     */
    public void attemptLogin(View view) {
        if (canLogin) {
            ((TextView) Objects.requireNonNull(views.get(R.id.username))).setError(null);
            ((TextView) Objects.requireNonNull(views.get(R.id.password))).setError(null);
            loginData = getData();
            boolean cancel = loginData.checkData(getApplicationContext(), views);

            if (!cancel) {
                progressShower.showProgress(true);
                try {
                    serverCommunicationThread.send(requestMaker.getNewRequest(new JSONd(ServicesFields.SERVICE, ServicesFields.LOGIN.toString()),
                            new JSONd(CommonFields.USERNAME, loginData.getUsername()),
                            new JSONd(CommonFields.PASSWORD, loginData.getPassword())));
                } catch (NotConnectedException e) {
                    progressShower.showProgress(false);
                    Toast.makeText(activity, getString(R.string.error_not_connected), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } else
            Toast.makeText(this, getString(R.string.encrypting_try_login), Toast.LENGTH_SHORT).show();
    }


    /* Retrieves data from text fields */
    private LoginData getData() {
        String username = ((TextView) Objects.requireNonNull(views.get(R.id.username))).getText().toString();
        String password = ((TextView) Objects.requireNonNull(views.get(R.id.password))).getText().toString();
        return new LoginData(username, password);
    }

    @Override
    public void onThreadStateChanged(final ServerCommunicationThreadState state) {
        Log.d(LOG_TAG, "OnThreadStateChanged");
        runOnUiThread(() -> updateThreadState(state));
    }

    private void updateThreadState(ServerCommunicationThreadState state) {
        TextView statusTextView = ((TextView) views.get(R.id.main_status));
        assert statusTextView != null;
        statusTextView.setText(getString(state.state));
        findViewById(R.id.log_in).setEnabled(connectionStateMap.getConnectionStateLogin(state));
        findViewById(R.id.not_registered).setEnabled(connectionStateMap.getConnectionStateRegistered(state));
    }

    private void getViews() {
        putViewIntoMap(R.id.main_status);
        putViewIntoMap(R.id.main_refresh);
        putViewIntoMap(R.id.username);
        putViewIntoMap(R.id.password);
        putViewIntoMap(R.id.rememberMeBox);
        putViewIntoMap(R.id.login_form);
        putViewIntoMap(R.id.login_progress);
    }

    private void putViewIntoMap(int id) {
        views.put(id, findViewById(id));
    }

    @Override
    public void onLoginComplete(boolean result) {
        if (result)
            saveRememberMeData();
        else
            showAlertDialog(activity.getString(R.string.login_failed));
        progressShower.showProgress(false);
    }

    private void showAlertDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(error).setTitle(activity.getString(R.string.dialog_error));
        builder.setPositiveButton(activity.getString(R.string.dialog_try_again), (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

}