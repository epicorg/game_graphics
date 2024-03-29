package game.net.handling;

import org.json.JSONObject;

import game.miscellaneous.Waiter;
import game.net.communication.JSONd;
import game.net.communication.NotConnectedException;
import game.net.communication.RequestMaker;
import game.net.communication.ServerCommunicationThread;
import game.net.fieldsnames.CommonFields;
import game.net.fieldsnames.GameFields;
import game.net.fieldsnames.RoomFields;
import game.net.fieldsnames.ServicesFields;
import game.player.Player;
import game.player.PlayerStatus;

/**
 * Class containing a thread which sends the current {@link Player}'s position and direction to the server.
 *
 * @author Torlaschi
 */
public class GamePositionSender implements Waiter {

    public static final long waitTime = 100;

    private final Player player;
    private final String roomName;

    private final ServerCommunicationThread serverCommunicationThread = ServerCommunicationThread.getInstance();

    private boolean sending = true;
    private final RequestMaker requestMaker = new RequestMaker();

    /**
     * Constructs a sender for the specified <code>Player</code>.
     *
     * @param player   <code>Player</code> containing the position and direction to send.
     * @param roomName room containing the specified <code>Player</code>.
     */
    public GamePositionSender(Player player, String roomName) {
        this.player = player;
        this.roomName = roomName;
    }

    /**
     * Set the status of the thread.
     * <p>
     * When stopped it can't be resumed.
     * </p>
     *
     * @param sending status of the thread
     */
    public void setSending(boolean sending) {
        this.sending = sending;
    }

    private final Runnable sendPositionRunnable = new Runnable() {
        @Override
        public void run() {
            while (sending) {
                PlayerStatus playerStatus = player.getStatus();
                Enum<?>[] names = new Enum[]{GameFields.GAME_X, GameFields.GAME_Y, GameFields.GAME_Z};
                JSONObject request = requestMaker.getNewRequest(new JSONd(ServicesFields.SERVICE, ServicesFields.GAME.toString()),
                        new JSONd(ServicesFields.SERVICE_TYPE, GameFields.GAME_POSITIONS.toString()),
                        new JSONd(CommonFields.USERNAME, player.getName()),
                        new JSONd(RoomFields.ROOM_NAME, roomName),
                        new JSONd(GameFields.GAME_POSITION, requestMaker.getNewRequest(names, playerStatus.getPosition())),
                        new JSONd(GameFields.GAME_DIRECTION, requestMaker.getNewRequest(names, playerStatus.getDirection()))
                );

                try {
                    serverCommunicationThread.send(request);
                } catch (NotConnectedException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void unleash() {
        new Thread(sendPositionRunnable).start();
    }

}
