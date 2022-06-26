package graphic.shadow.system;

/**
 * Something which can be initialized.
 *
 * @author Alessandro Martinelli
 * @pattern interface
 */
public interface SFInitiable {

    /**
     * initialize this SFInitiable
     */
    void init();

    /**
     * Destroy this SFInitiable
     */
    void destroy();

}
