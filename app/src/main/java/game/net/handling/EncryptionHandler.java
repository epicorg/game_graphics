package game.net.handling;

import android.os.Handler;
import android.os.Message;

import game.net.data.OperationStarter;

/**
 * {@link Handler} for handle encryption state.
 */
public class EncryptionHandler extends Handler {

    private final OperationStarter operationStarter;

    /**
     * Creates a new <code>EncryptionHandler</code>.
     *
     * @param operationStarter the {@link OperationStarter} which start when ended encryption.
     */
    public EncryptionHandler(OperationStarter operationStarter) {
        this.operationStarter = operationStarter;
    }

    @Override
    public void handleMessage(Message msg) {
        if ((boolean) msg.obj)
            operationStarter.start(null);
    }

}
