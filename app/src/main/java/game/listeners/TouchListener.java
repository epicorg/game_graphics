package game.listeners;

import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import game.controls.ButtonsControl;

/**
 * Created by Andrea on 01/04/2015.
 */
public class TouchListener implements TouchListenerInterface {

    public static final String LOG_TAG = "TouchListener";

    private GLSurfaceView surfaceView;
    private ButtonsControl buttonsControl;
    private PositionMoveListenerInterface positionMoveListener;
    private DirectionMoveListenerInterface directionMoveListener;

    private boolean isPressing = false;
    private float previousX, previousY;
    private float touchX, touchY;
    private ButtonsControl.ButtonPositions positions;

    public TouchListener(GLSurfaceView surfaceView, ButtonsControl buttonsControl, PositionMoveListenerInterface positionMoveListener, DirectionMoveListenerInterface directionMoveListener) {
        this.surfaceView = surfaceView;
        this.buttonsControl = buttonsControl;
        this.positionMoveListener = positionMoveListener;
        this.directionMoveListener = directionMoveListener;
    }

    @Override
    public void onTouchEvent(final MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                surfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        if (buttonsControl.isInsideAButton(touchX, surfaceView.getHeight() - touchY) && !isPressing) {
                            isPressing = true;
                            positions=buttonsControl.getPressedButton(touchX, surfaceView.getHeight() - touchY);
                            new Thread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("thread","started for moving "+positions);
                                            while(isPressing){
                                                Log.d("loop","moved "+positions);
                                                callPositionListener(positions);
                                                try{
                                                    Thread.sleep(200);
                                                }catch (InterruptedException e){
                                                    e.printStackTrace();
                                                }
                                            }
                                            Log.d("thread","ended for moving "+positions);
                                        }
                                    }
                            ).start();
                        } else {
                            previousX = touchX;
                            previousY = touchY;
                        }
                    }
                });
            case MotionEvent.ACTION_UP:
                if (isPressing) {
                    isPressing = false;
                    surfaceView.queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            callPositionListener(buttonsControl.getPressedButton(touchX, surfaceView.getHeight() - touchY));
                        }
                    });
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isPressing) {
                    float dx = touchX - previousX;
                    float dy = touchY - previousY;
                    previousX = touchX;
                    previousY = touchY;

                    directionMoveListener.move(dx, dy);
                    Log.d(LOG_TAG, "Moved of dx: " + dx + ", dy: " + dy);
                }
                break;
        }
    }

    private void callPositionListener(ButtonsControl.ButtonPositions buttonPositions) {
        switch (buttonPositions) {
            case LEFT:
                positionMoveListener.move((float) +Math.PI / 2, 0);
                break;
            case RIGHT:
                positionMoveListener.move((float) -Math.PI / 2, 0);
                break;
            case UP:
                positionMoveListener.move(0, 0);
                break;
            case DOWN:
                positionMoveListener.move((float) -Math.PI, 0);
                break;
        }
    }

}
