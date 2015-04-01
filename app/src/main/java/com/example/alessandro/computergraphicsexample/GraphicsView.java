package com.example.alessandro.computergraphicsexample;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import game.controls.ButtonsControl;
import game.generators.ButtonsGenerator;
import game.generators.FundamentalGenerator;
import game.generators.GroundGenerator;
import game.graphics.Map;
import game.graphics.Sky;
import game.listeners.DirectionDirectionMoveListener;
import game.listeners.DirectionMoveListenerInterface;
import game.listeners.PositionMoveListenerInterface;
import game.listeners.PositionMoveListenerXZWithCollisions;
import game.listeners.TouchListener;
import game.listeners.TouchListenerInterface;
import game.physics.Box;
import game.physics.CollisionMediator;
import game.physics.Wall;
import game.player.Player;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLSystemState;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;
import shadow.math.SFVertex3f;

import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.frustumM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.setLookAtM;

/**
 * Created by Alessandro on 13/03/15.
 */
public class GraphicsView extends GLSurfaceView {

    public static final String LOG_TAG = "GraphicsView";
    private final float[] orthoMatrix = new float[16];
    private final float[] resultMatrix = new float[16];
    private GLSurfaceView surfaceView;
    private Context context;
    private ShadingProgram program;
    private Player me;
    private ArrayList<Player> otherPlayers;
    private TouchListenerInterface touchListener;
    private ButtonsGenerator buttonsGenerator;
    private ButtonsControl buttonsControl;
    private PositionMoveListenerInterface positionMoveListener;
    private DirectionMoveListenerInterface directionMoveListener;

    private CollisionMediator cm = new CollisionMediator();


    public GraphicsView(Context context, Player me, ArrayList<Player> otherPlayers) {
        super(context);
        setEGLContextClientVersion(2);

        this.surfaceView = this;

        this.context = context;
        this.me = me;
        this.otherPlayers = otherPlayers;

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(new GraphicsRenderer());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchListener.onTouchEvent(event);
        return true;
    }

    public class GraphicsRenderer implements Renderer {
        private Sky sky;
        private Map map;

        private Node node;
        private ArrayList<Node> groundNodes;
        private ArrayList<Node> buttonsNodes;

        private float t = 0;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            ShadersKeeper.loadPipelineShaders(context);
            program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);

            sky = new Sky(context, program, me.getStatus().getPosition());
            map = new Map(context);

            Model monkeyModel = FundamentalGenerator.getModel(context, program, R.drawable.animal_texture_01, "Monkey.obj");

            node = new Node();
            node.setModel(monkeyModel);
            node.getRelativeTransform().setPosition(0, 0.5f, 0);

            Node anotherNode = new Node();

            anotherNode.setModel(monkeyModel);
            anotherNode.getRelativeTransform().setPosition(1, 1, 0);
            anotherNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.3f, 0.2f, 0.1f));
            node.getSonNodes().add(anotherNode);

            GroundGenerator groundGenerator = new GroundGenerator(FundamentalGenerator.getModel(context, program, R.drawable.ground_texture_01, "Ground.obj"));
            groundNodes = groundGenerator.getGround(0, 0, 15, 15, -1);

            map.addObjects("Wall.obj", R.drawable.wall_texture_02,
                    new Wall(new SFVertex3f(4, 0, -1), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(4, 0, -2), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(4, 0, -3), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(3, 0, -4), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(2, 0, -4), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(1, 0, -4), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(-1, 0, -4), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(-1, 0, -3), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(-1, 0, 0), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(-2, 0, 0), new Box(1, 2, 1)),
                    new Wall(new SFVertex3f(-3, 0, 0), new Box(1, 2, 1)));
            map.load(cm);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);

            positionMoveListener = new PositionMoveListenerXZWithCollisions(me.getStatus().getPosition(), me.getStatus().getDirection(), cm, me.getStatus().getBox());
            directionMoveListener = new DirectionDirectionMoveListener(me.getStatus().getDirection(), getWidth(), getHeight());

            Model arrowModel = FundamentalGenerator.getModel(context, program, R.drawable.arrow_texture_02, "Arrow.obj");
            buttonsGenerator = new ButtonsGenerator(arrowModel);
            buttonsControl = new ButtonsControl(context, program, orthoMatrix, buttonsGenerator.getLeftNode(), buttonsGenerator.getRightNode(), buttonsGenerator.getUpNode(), buttonsGenerator.getDownNode());
            touchListener = new TouchListener(surfaceView, buttonsControl, positionMoveListener, directionMoveListener);

            buttonsNodes = buttonsGenerator.getButtons();
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            setMatrices(getWidth(), getHeight(), (float) getWidth() / getHeight());
            program.setupProjection(resultMatrix);
            SFOGLSystemState.cleanupColorAndDepth(0, 0, 1, 1);

            //Change the Node transform
            t += 0.01f;
            float rotation = 0.2f + t;
            float scaling = 0.3f;
            SFMatrix3f matrix3f = SFMatrix3f.getScale(scaling, scaling, scaling);
            matrix3f = matrix3f.MultMatrix(SFMatrix3f.getRotationX(rotation));
            node.getRelativeTransform().setMatrix(matrix3f);
            node.updateTree(new SFTransform3f());
            node.draw();

            for (Node groundNode : groundNodes) {
                groundNode.updateTree(new SFTransform3f());
                groundNode.draw();
            }

            map.draw();
            sky.draw();

            program.setupProjection(orthoMatrix);

            for (Node buttonNode : buttonsNodes) {
                buttonNode.updateTree(new SFTransform3f());
                buttonNode.draw();
            }
        }

        private void setMatrices(int width, int height, float ratio) {
            setResultMatrix(ratio);
            setOrthoMatrix(width, height, ratio);
        }

        private void setOrthoMatrix(int width, int height, float ratio) {
            if (width > height) {
                orthoM(orthoMatrix, 0, -ratio, ratio, -1, 1, -1, 1);
            } else {
                orthoM(orthoMatrix, 0, -1, 1, -(1 / ratio), (1 / ratio), -1, 1);
            }
        }

        private void setResultMatrix(float ratio) {
            final float[] viewMatrix = new float[16];
            final float[] projectionMatrix = new float[16];
            setViewMatrix(viewMatrix);
            frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 0.7f, 50);
            multiplyMM(resultMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        }

        private void setViewMatrix(float[] viewMatrix) {
            float eyeX, eyeY, eyeZ, centerX, centerY, centerZ;
            eyeX = me.getStatus().getPosition().getX();
            eyeY = me.getStatus().getPosition().getY();
            eyeZ = me.getStatus().getPosition().getZ();
            centerX = me.getStatus().getPosition().getX() + me.getStatus().getDirection().getX();
            centerY = me.getStatus().getPosition().getY() + me.getStatus().getDirection().getY();
            centerZ = me.getStatus().getPosition().getZ() + me.getStatus().getDirection().getZ();

            setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
        }
    }

}
