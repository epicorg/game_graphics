package game.net.interpreters;

import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;

import epic.org.R;
import game.net.fieldsnames.GameFields;
import game.net.handling.GameHandlerListener;
import game.net.handling.GamePositionSender;
import game.net.services.Game;
import game.views.MessageScreen;

/**
 * Responsible for interpreting {@link Message} about game status data.
 *
 * @see MessageScreen
 * @see GamePositionSender
 * @see GameHandlerListener
 */
public class StatusInterpreter implements Interpreter {

    private static final String LOG_TAG = "StatusInterpreter";

    private final MessageScreen messageScreen;
    private final GamePositionSender gamePositionSender;
    private final Resources res;
    private final LinkedList<GameHandlerListener> gameHandlerListeners;

    /**
     * Creates a new <code>StatusInterpreter</code>.
     *
     * @param messageScreen        <code>MessageScreen</code> that shows the status.
     * @param gamePositionSender   <code>GamePositionSender/code> to stop sending position data.
     * @param gameHandlerListeners <code>GameHandlerListener</code> to call at game start and end.
     */
    public StatusInterpreter(Context context, MessageScreen messageScreen, GamePositionSender gamePositionSender,
                             GameHandlerListener... gameHandlerListeners) {
        this.res = context.getResources();
        this.messageScreen = messageScreen;
        this.gamePositionSender = gamePositionSender;
        this.gameHandlerListeners = new LinkedList<>(Arrays.asList(gameHandlerListeners));
    }

    @Override
    public int getKey() {
        return Game.STATUS;
    }

    @Override
    public void interpret(Message message) {
        Game.GameStatusResult results = (Game.GameStatusResult) message.obj;

        if (results.go)
            Log.d(LOG_TAG, "Go");

        for (GameHandlerListener l : gameHandlerListeners)
            l.onGameGo();

        if (results.gameEnd != null) {
            Log.d(LOG_TAG, "Game End");
            String gameEnd = results.gameEnd;
            switch (GameFields.valueOf(gameEnd)) {
                case GAME_WIN:
                    messageScreen.setText(res.getString(R.string.messageWin), res.getColor(R.color.mWinCol));
                    break;
                case GAME_DRAW:
                    messageScreen.setText(res.getString(R.string.messageDraw), res.getColor(R.color.mDrawCol));
                    break;
                case GAME_LOSE:
                    messageScreen.setText(res.getString(R.string.messageLose), res.getColor(R.color.mLoseCol));
                    break;
                case GAME_INTERRUPTED:
                    messageScreen.setText(res.getString(R.string.messageInterrupted), res.getColor(R.color.mInterruptedCol));
                    break;
            }

            gamePositionSender.setSending(false);
            messageScreen.show();

            new Thread(() -> {
                try {
                    Thread.sleep(res.getInteger(R.integer.endWaitTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (GameHandlerListener l : gameHandlerListeners)
                    l.onGameFinish();
            }).start();
        }
    }

}
