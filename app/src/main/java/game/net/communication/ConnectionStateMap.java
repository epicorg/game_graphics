package game.net.communication;

import static game.net.communication.ServerCommunicationThreadState.CONNECTED;
import static game.net.communication.ServerCommunicationThreadState.CONNECTING;
import static game.net.communication.ServerCommunicationThreadState.ENCRYPTING;
import static game.net.communication.ServerCommunicationThreadState.NOT_CONNECTED;

import java.util.HashMap;
import java.util.Objects;

/**
 * Class that maps the {@link ServerCommunicationThreadState} to corresponding state values.
 */
public class ConnectionStateMap {

    private final HashMap<ServerCommunicationThreadState, ConnectionStateValues> map;

    public ConnectionStateMap() {
        map = new HashMap<>();
        map.put(NOT_CONNECTED, new ConnectionStateValues(false, false));
        map.put(CONNECTING, new ConnectionStateValues(false, false));
        map.put(ENCRYPTING, new ConnectionStateValues(false, false));
        map.put(CONNECTED, new ConnectionStateValues(true, true));
    }

    public boolean getConnectionStateLogin(ServerCommunicationThreadState state) {
        return Objects.requireNonNull(map.get(state)).loginEnabled;
    }

    public boolean getConnectionStateRegistered(ServerCommunicationThreadState state) {
        return Objects.requireNonNull(map.get(state)).notRegistered;
    }

    private static class ConnectionStateValues {

        private final boolean loginEnabled, notRegistered;

        public ConnectionStateValues(boolean loginEnabled, boolean notRegistered) {
            this.loginEnabled = loginEnabled;
            this.notRegistered = notRegistered;
        }
    }

}
