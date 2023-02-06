package epic.org;

import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import game.net.communication.JSONd;
import game.net.communication.NotConnectedException;
import game.net.communication.RequestMaker;
import game.net.communication.ServerCommunicationThread;
import game.net.data.RegistrationData;
import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.RegisterFields;
import game.net.fieldsnames.ServicesFields;
import game.net.interaction.ProgressShower;
import game.net.interaction.RegistrationErrorStrings;
import game.net.services.Register;

/**
 * A login screen where the user can register with username, email and password.
 */
public class RegistrationActivity extends AppCompatActivity {

    /**
     * Keeps track of the login task to ensure we can cancel it if requested.
     */
    private final RegistrationActivity thisActivity = this;
    private ServerCommunicationThread serverCommunicationThread;
    private final HashMap<Integer, View> views = new HashMap<>();
    private ProgressShower progressShower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getViews();
        serverCommunicationThread = ServerCommunicationThread.getInstance();

        progressShower = new ProgressShower(views.get(R.id.login_progress), views.get(R.id.registration_form),
                getResources().getInteger(android.R.integer.config_shortAnimTime));

        requireNonNull(getSupportActionBar()).setTitle(getString(R.string.registration_title));

        findViewById(R.id.registrationButton).setOnClickListener(this::attemptRegistration);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverCommunicationThread.setHandler(new RegistrationHandler());
    }

    private void getViews() {
        views.put(R.id.email, (View) findViewById(R.id.email));
        views.put(R.id.username, (View) findViewById(R.id.username));
        views.put(R.id.password, (View) findViewById(R.id.password));
        views.put(R.id.registration_form, (View) findViewById(R.id.registration_form));
        views.put(R.id.login_progress, (View) findViewById(R.id.login_progress));
        views.put(R.id.confirm_password, (View) findViewById(R.id.confirm_password));
    }

    /**
     * Attempts to sign in or register the account specified by the login form. If there are form errors
     * (invalid email, missing fields, etc.), the errors are presented and no actual login attempt is made.
     */
    public void attemptRegistration(View view) {
        // Reset errors
        ((EditText) requireNonNull(views.get(R.id.email))).setError(null);
        ((EditText) requireNonNull(views.get(R.id.password))).setError(null);
        ((EditText) requireNonNull(views.get(R.id.username))).setError(null);
        ((EditText) requireNonNull(views.get(R.id.confirm_password))).setError(null);

        RegistrationData registrationData = getRegistrationData();

        boolean cancel = registrationData.checkData(getApplicationContext(), views);

        if (!cancel) {
            progressShower.showProgress(true);
            try {
                serverCommunicationThread.send(requestMaker.getNewRequest(
                        new JSONd(ServicesFields.SERVICE, ServicesFields.REGISTER.toString()),
                        new JSONd(RegisterFields.EMAIL, registrationData.getEmail()),
                        new JSONd(CommonFields.USERNAME, registrationData.getUsername()),
                        new JSONd(CommonFields.PASSWORD, registrationData.getPassword())));
            } catch (NotConnectedException e) {
                Toast.makeText(thisActivity, getString(R.string.error_not_connected), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private final RequestMaker requestMaker = new RequestMaker();

    /* Retrieves data from text fields */
    private RegistrationData getRegistrationData() {
        String email = ((EditText) requireNonNull(views.get(R.id.email))).getText().toString();
        String username = ((EditText) requireNonNull(views.get(R.id.username))).getText().toString();
        String password = ((EditText) requireNonNull(views.get(R.id.password))).getText().toString();
        String confirmPassword = ((EditText) requireNonNull(views.get(R.id.confirm_password))).getText().toString();
        return new RegistrationData(username, email, password, confirmPassword);
    }

    @SuppressLint("HandlerLeak")
    public class RegistrationHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Register.RegistrationResult result = (Register.RegistrationResult) msg.obj;
            if (result.isOk()) {
                finish();
            } else {
                ArrayList<String> errors = result.getErrors();
                showAlertDialog(generateErrorString(errors));
            }
            progressShower.showProgress(false);
            Log.d("RESULT", String.valueOf(result.isOk()));
        }

    }

    private void showAlertDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
        builder.setMessage(error).setTitle(getString(R.string.dialog_error));
        builder.setPositiveButton(getString(R.string.dialog_try_again), (dialog, which) -> dialog.cancel());
        builder.create().show();
    }

    private String generateErrorString(ArrayList<String> errors) {
        StringBuilder error = new StringBuilder();
        RegistrationErrorStrings strings = new RegistrationErrorStrings();
        for (String string : errors)
            error.append(getString(strings.getStringIdByError(string))).append("\n");
        return error.toString();
    }

}
