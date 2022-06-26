package graphic.sfogl2;

/**
 * A sequence of SFOGLState
 *
 * @author Alessandro Martinelli
 */
public class SFOGLCompositeState implements SFOGLState {

    private final SFOGLState[] states;

    public SFOGLCompositeState(SFOGLState[] states) {
        super();
        this.states = states;
    }

    @Override
    public void applyState() {
        for (SFOGLState state : states)
            state.applyState();
    }

}
