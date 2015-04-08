package game.listeners;

import android.opengl.GLSurfaceView;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import game.controls.ButtonPositions;
import game.controls.ButtonsControl;

/**
 * Created by Andrea on 01/04/2015.
 */
public class TouchListener implements TouchListenerInterface {

    public static final String LOG_TAG = "TouchListener";
    public static final long TIME_SLEEP = 10;

    private GLSurfaceView surfaceView;
    private ButtonsControl buttonsControl;
    private PositionMoveListenerInterface positionMoveListener;
    private DirectionMoveListenerInterface directionMoveListener;

    private int positionId = -1;
    private boolean isPressing = false;
    private int directionId = -1;
    private boolean isMoving = false;

    private float previousX, previousY;
    private ButtonPositions positions;

    public TouchListener(GLSurfaceView surfaceView, ButtonsControl buttonsControl, PositionMoveListenerInterface positionMoveListener, DirectionMoveListenerInterface directionMoveListener) {
        this.surfaceView = surfaceView;
        this.buttonsControl = buttonsControl;
        this.positionMoveListener = positionMoveListener;
        this.directionMoveListener = directionMoveListener;
    }

    @Override
    public void onTouchEvent(final MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        final int index = MotionEventCompat.getActionIndex(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                final float touchX = MotionEventCompat.getX(event, index);
                final float touchY = MotionEventCompat.getY(event, index);

                surfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        if (buttonsControl.isInsideAButton(touchX, surfaceView.getHeight() - touchY) && !isPressing) {
                            isPressing = true;
                            try {
                                positionId = MotionEventCompat.getPointerId(event, index);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            positions = buttonsControl.getPressedButton(touchX, surfaceView.getHeight() - touchY);
                            new Thread(new MoveRunnable()).start();
                        } else if (!isMoving) {
                            isMoving = true;
                            try {
                                directionId = MotionEventCompat.getPointerId(event, index);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            previousX = touchX;
                            previousY = touchY;
                        }
                    }
                });
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 1) {
                    isPressing = false;
                    positionId = -1;
                    
                    isMoving = false;
                    directionId = -1;
                } else if (isPressing && (MotionEventCompat.getPointerId(event, index) == positionId)) {
                    isPressing = false;
                    positionId = -1;
                } else if (isMoving && (MotionEventCompat.getPointerId(event, index) == directionId)) {
                    isMoving = false;
                    directionId = -1;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoving) {
                    actionMoveDirectionProcessor(event);
                }
                break;
        }
    }

    private void actionMoveDirectionProcessor(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            if (event.getPointerId(i) == directionId) {
                final float touchX = MotionEventCompat.getX(event, i);
                final float touchY = MotionEventCompat.getY(event, i);

                float dx = touchX - previousX;
                float dy = touchY - previousY;
                previousX = touchX;
                previousY = touchY;

                directionMoveListener.move(dx, dy);
                return;
            }
        }
    }

    private void callPositionListener(ButtonPositions buttonPositions, long delta) {
        switch (buttonPositions) {
            case LEFT:
                positionMoveListener.move((float) +Math.PI / 2, 0, delta);
                break;
            case RIGHT:
                positionMoveListener.move((float) -Math.PI / 2, 0, delta);
                break;
            case UP:
                positionMoveListener.move(0, 0, delta);
                break;
            case DOWN:
                positionMoveListener.move((float) -Math.PI, 0, delta);
                break;
        }
    }

    private class MoveRunnable implements Runnable {

        @Override
        public void run() {
            while (isPressing) {
                callPositionListener(positions, TIME_SLEEP);
                try {
                    Thread.sleep(TIME_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
