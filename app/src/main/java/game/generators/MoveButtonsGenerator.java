package game.generators;

import android.content.Context;

import epic.org.R;
import game.controls.Button;
import game.controls.ButtonMaster;
import game.graphics.MaterialKeeper;
import game.listeners.PositionMoveListenerInterface;
import graphic.integration.Model;
import graphic.integration.Node;
import graphic.integration.ShadingProgram;
import graphic.shadow.math.SFMatrix3f;
import graphic.shadow.math.SFVertex3f;

/**
 * This class creates the 4 {@link Button} of movement.
 *
 * @author De Pace
 * @see ButtonMaster
 */
public class MoveButtonsGenerator {

    private final Context context;
    private final ShadingProgram program;
    private final ButtonMaster buttonMaster;
    private final PositionMoveListenerInterface positionMoveListener;

    /**
     * Creates a new <code>MoveButtonsGenerator</code>.
     *
     * @param context              <code>Context</code> to find resources
     * @param program              <code>ShadingProgram</code> to represent the <code>Button</code>
     * @param buttonMaster         <code>ButtonMaster</code> which the movement <code>Button</code> has to be associated with
     * @param positionMoveListener interface through which the movement action is associated with <code>Button</code>
     */
    public MoveButtonsGenerator(Context context, ShadingProgram program, ButtonMaster buttonMaster, PositionMoveListenerInterface positionMoveListener) {
        this.context = context;
        this.program = program;
        this.buttonMaster = buttonMaster;
        this.positionMoveListener = positionMoveListener;
    }

    /**
     * Generates the movement <code>Button</code> in a cross configuration.
     *
     * @param center   central position of the <code>Button</code>
     * @param scale    scale factor
     * @param distance distance between movement <code>Button</code>
     */
    public void generate(SFVertex3f center, float scale, float distance) {
        Node parentNode = new Node();
        SFMatrix3f scalingMatrix = SFMatrix3f.getScale(scale, scale, scale);

        parentNode.getRelativeTransform().setPosition(center);
        parentNode.getRelativeTransform().setMatrix(scalingMatrix);

        int color = context.getResources().getColor(R.color.primary);
        Model model = FundamentalGenerator.getModelFromFileAndMaterial(context, MaterialKeeper.MATERIAL_KEEPER.getColorMaterial(program, color), "Arrow.obj");

        buttonMaster.setModel(model);
        buttonMaster.addButton(new Button("LEFT", parameter ->
                        positionMoveListener.move((float) Math.PI / 2, 0, (long) parameter), true, true),
                new SFVertex3f(-distance, 0, 0), (float) -Math.PI / 2, parentNode);

        buttonMaster.addButton(new Button("RIGHT", parameter ->
                        positionMoveListener.move((float) -Math.PI / 2, 0, (long) parameter), true, true),
                new SFVertex3f(distance, 0, 0), (float) Math.PI / 2, parentNode);

        buttonMaster.addButton(new Button("UP", parameter ->
                        positionMoveListener.move(0, 0, (long) parameter), true, true),
                new SFVertex3f(0, distance, 0), (float) 0, parentNode);

        buttonMaster.addButton(new Button("DOWN", parameter ->
                        positionMoveListener.move((float) -Math.PI, 0, (long) parameter), true, true),
                new SFVertex3f(0, -distance, 0), (float) -Math.PI, parentNode);
    }

}
