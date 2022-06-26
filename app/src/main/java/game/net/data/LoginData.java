package game.net.data;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import epic.org.R;

/**
 * This class gets the username and the password the user inserted.
 *
 * @author Micieli
 * @date 27/03/2015
 */
public class LoginData {

    public static final int PASSWORD_MIN_LENGTH = 8;

    private final String username;
    private final String password;

    /**
     * Creates a new <code>LoginData</code> with given username and password.
     *
     * @param username name of the user.
     * @param password password of the user.
     */
    public LoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private boolean isPasswordLongEnough() {
        return password.length() >= PASSWORD_MIN_LENGTH;
    }

    private boolean isPasswordValid() {
        return password.matches(".*\\d+.*") && password.matches(".*[a-zA-Z]+.*");
    }

    /**
     * @return the user name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the user password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Checks the data: it throws specific exception if a format is not valid.
     */
    public boolean checkData(Context context, HashMap<Integer, View> views) {
        boolean cancel = false;

        TextView passwordField = (TextView) views.get(R.id.password);
        TextView userField = (TextView) views.get(R.id.username);

        if (!isPasswordValid()) {
            assert passwordField != null;
            passwordField.setError(context.getString(R.string.error_invalid_password));
            passwordField.requestFocus();
            cancel = true;
        }

        if (!TextUtils.isEmpty(getPassword()) && !isPasswordLongEnough()) {
            assert passwordField != null;
            passwordField.setError(context.getString(R.string.error_short_password));
            passwordField.requestFocus();
            cancel = true;
        }

        if (TextUtils.isEmpty(getUsername())) {
            assert userField != null;
            userField.setError(context.getString(R.string.error_field_required));
            userField
                    .requestFocus();
            cancel = true;
        }
        return cancel;
    }

}
