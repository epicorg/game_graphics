package game.net.data;

/**
 * Interface for a class that has some operation that should remotely be started.
 *
 * @author De Pace
 */
public interface OperationStarter {

    /**
     * Starts an operation.
     *
     * @param parameter Argument for the operation.
     */
    void start(Object parameter);

}
