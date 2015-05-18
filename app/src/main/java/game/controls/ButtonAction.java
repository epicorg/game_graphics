package game.controls;

/**
 * Interfaccia per oggetti che incapsulino un'azione.
 */
public interface ButtonAction {

    /**
     * Azione invocabile.
     * @param parameter Generico parametro per una azione.
     */
    void action(Object parameter);

}
