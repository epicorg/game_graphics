package game.controls;

/**
 * A button that performs an action which is encapsulated in a {@link ButtonAction}.
 *
 * @author De Pace
 * @see ButtonAction
 */
public class Button {

    private final String name;
    private final ButtonAction action;
    private final boolean continuousPressing;
    private final boolean needToBeReady;

    /**
     * Creates a new <code>Button</code>.
     *
     * @param name               <code>Button</code>'s name
     * @param action             <code>ButtonAction</code> which represents the action to perform when pressed
     * @param continuousPressing indicates if the <code>Button</code> operates with continuous pressure
     * @param needToBeReady      marks a <code>Button</code> that can be disabled when needed
     */
    public Button(String name, ButtonAction action, boolean continuousPressing, boolean needToBeReady) {
        this.name = name;
        this.action = action;
        this.continuousPressing = continuousPressing;
        this.needToBeReady = needToBeReady;
    }

    /**
     * @return the <code>Button</code>'s name
     */
    public String getName() {
        return name;
    }

    /**
     * Performs the action which is encapsulated in the <code>ButtonAction</code>.
     *
     * @param parameter contingent parameter for the action.
     */
    public void execute(Object parameter) {
        action.action(parameter);
    }

    /**
     * @return 'true' if the <code>Button</code> operates with continuous pressure
     */
    public boolean isContinuousPressing() {
        return continuousPressing;
    }

    /**
     * @return 'true' if the <code>Button</code> can be disabled
     */
    public boolean isNeedToBeReady() {
        return needToBeReady;
    }

}
