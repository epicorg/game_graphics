package sfogl2;


/**
 * A sequence of SFOGLState
 *
 * @author Alessandro Martinelli
 */
public class SFOGLCompositeState implements SFOGLState {

    private SFOGLState[] states;

    public SFOGLCompositeState(SFOGLState[] states) {
        super();
        this.states = states;
    }

    ;

    @Override
    public void applyState() {
        for (int i = 0; i < states.length; i++) {
            states[i].applyState();
        }
    }
}
