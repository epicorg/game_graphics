package shadow.system;

/**
 * Something which can be initialized
 *
 * @author Alessandro Martinelli
 * @pattern interface
 */
public interface SFInitiable {
    /**
     * initialize this SFInitiable
     */
    public void init();

    /**
     * Destroy this SFInitiable
     */
    public void destroy();
}
