package game.net.handling;

/**
 * Listener for Login {@link android.os.Handler}.
 *
 * @author Torlaschi
 * @date 22/04/2015
 */
public interface LoginHandlerListener {

    /**
     * Called on finishing login operation.
     *
     * @param result indicates whether the login operation were successful
     */
    void onLoginComplete(boolean result);

}
