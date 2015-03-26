package com.example.alessandro.computergraphicsexample;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import generators.GroundGenerator;
import objLoader.ObjLoader;
import sfogl.integration.ArrayObject;
import sfogl.integration.BitmapTexture;
import sfogl.integration.Material;
import sfogl.integration.Mesh;
import sfogl.integration.Model;
import sfogl.integration.Node;
import sfogl.integration.ShadingProgram;
import sfogl2.SFOGLSystemState;
import sfogl2.SFOGLTextureModel;
import shadow.graphics.SFImageFormat;
import shadow.math.SFMatrix3f;
import shadow.math.SFTransform3f;

import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.frustumM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setLookAtM;

/**
 * Created by Alessandro on 13/03/15.
 */
public class GraphicsView extends GLSurfaceView {

    private final float[] resultMatrix = new float[16];
    private Context context;
    private WindowManager windowManager;

    public GraphicsView(Context context, WindowManager windowManager) {
        super(context);
        setEGLContextClientVersion(2);

        this.context = context;
        this.windowManager = windowManager;

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(new GraphicsRenderer());
    }

    public class GraphicsRenderer implements Renderer {

        private Node node;
        private ArrayList<Node> groundNodes;
        private float t = 0;
        private ShadingProgram program;

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //Step 1 : load Shading effects
            ShadersKeeper.loadPipelineShaders(context);
            program = ShadersKeeper.getProgram(ShadersKeeper.STANDARD_TEXTURE_SHADER);

            //Step 2 : load Textures
            int textureModel = SFOGLTextureModel.generateTextureObjectModel(SFImageFormat.RGB, GLES20.GL_REPEAT, GLES20.GL_REPEAT, GLES20.GL_LINEAR, GLES20.GL_LINEAR);
            BitmapTexture texture = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(), R.drawable.paddedroom_texture_01), textureModel);
            texture.init();
            BitmapTexture groundTexture = BitmapTexture.loadBitmapTexture(BitmapFactory.decodeResource(context.getResources(), R.drawable.ground_texture_01), textureModel);
            groundTexture.init();

            //Step 3 : create a Material (materials combine shaders+textures+shading parameters)
            Material material = new Material(program);
            material.getTextures().add(texture);

            //Step 4: load a Geometry
            ArrayObject[] objects = ObjLoader.arrayObjectFromFile(context, "MonkeyTxN.obj");

            Mesh mesh = new Mesh(objects[0]);
            mesh.init();

            //Step 5: create a Model combining material+geometry
            Model monkeyModel = new Model();
            monkeyModel.setRootGeometry(mesh);
            monkeyModel.setMaterialComponent(material);

            //Step 6: create a Node, that is a reference system where you can place your Model
            node = new Node();
            node.setModel(monkeyModel);
            node.getRelativeTransform().setPosition(0, 0.5f, 0);

            Node anotherNode = new Node();
            anotherNode.setModel(monkeyModel);
            anotherNode.getRelativeTransform().setPosition(1, 1, 0);
            anotherNode.getRelativeTransform().setMatrix(SFMatrix3f.getScale(0.3f, 0.2f, 0.1f));
            node.getSonNodes().add(anotherNode);

            GroundGenerator groundGenerator = new GroundGenerator(context, program, R.drawable.ground_texture_01, "Ground.obj");
            groundNodes = groundGenerator.getGround(0, 0, 3, 3, -1);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);

            final float[] viewMatrix = new float[16];
            final float[] projectionMatrix = new float[16];
            setLookAtM(viewMatrix, 0, 0, +1, +2, 0, 0.5f, 1, 0, 1, 0);
            float ratio = (float) width / height;
            frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
            multiplyMM(resultMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            SFOGLSystemState.cleanupColorAndDepth(0, 0, 1, 1);

            program.setupProjection(resultMatrix);

            //Change the Node transform
            t += 0.01f;
            float rotation = 0.2f + t;
            float scaling = 0.3f;
            SFMatrix3f matrix3f = SFMatrix3f.getScale(scaling, scaling, scaling);
            matrix3f = matrix3f.MultMatrix(SFMatrix3f.getRotationX(rotation));
            node.getRelativeTransform().setMatrix(matrix3f);
            node.updateTree(new SFTransform3f());

            //Change the Node transform
            scaling = 1.0f;
            matrix3f = SFMatrix3f.getScale(scaling, scaling, scaling);

            //Draw nodes
            node.draw();

            for (Node groundNode : groundNodes) {
                groundNode.getRelativeTransform().setMatrix(matrix3f);
                groundNode.updateTree(new SFTransform3f());
                groundNode.draw();
            }
        }
    }
}
