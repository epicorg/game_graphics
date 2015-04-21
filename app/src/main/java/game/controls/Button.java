package game.controls;

/**
 * Created by depa on 21/04/15.
 */
public class Button {

    private ButtonAction action;

    public Button(ButtonAction action){
        this.action=action;
    }

    public void execute(Object parameter){
        action.action(parameter);
    }
}
