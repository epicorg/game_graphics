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
    private volatile boolean isPressing = false;
    private int directionId = -1;
    private volatile boolean isMoving = false;

    private float previousX, previousY;
    private Thread moveThread;
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

                if (buttonsControl.isInsideAButton(touchX, touchY) && !isPressing) {
                    startPressing(event, index, touchX, touchY);
                } else if (!isMoving) {
                    startMoving(event, index, touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 1) {
                    stopPressing();
                    stopMoving();
                } else if (isPressing && (MotionEventCompat.getPointerId(event, index) == positionId)) {
                    stopPressing();
                } else if (isMoving && (MotionEventCompat.getPointerId(event, index) == directionId)) {
                    stopMoving();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                actionMovePositionProcessor(event);
                actionMoveDirectionProcessor(event);
                break;
        }
    }

    private void actionMovePositionProcessor(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            if (event.getPointerId(i) == positionId && isPressing) {
                final float touchX = MotionEventCompat.getX(event, i);
                final float touchY = MotionEventCompat.getY(event, i);

                if (!buttonsControl.isInsideAButton(touchX, touchY)) {
                    stopPressing();
                }
            } else if (event.getPointerId(i) != directionId && !isPressing) {
                final float touchX = MotionEventCompat.getX(event, i);
                final float touchY = MotionEventCompat.getY(event, i);

                if (buttonsControl.isInsideAButton(touchX, touchY)) {
                    startPressing(event, i, touchX, touchY);
                }
            }
        }
    }

    private void actionMoveDirectionProcessor(MotionEvent event) {
        if (isMoving) {
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

    private void startPressing(MotionEvent event, int index, float touchX, float touchY) {
        isPressing = true;
        positionId = MotionEventCompat.getPointerId(event, index);

        positions = buttonsControl.getPressedButton(touchX, touchY);
        moveThread = new Thread(new Runnable() {
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
        });
        moveThread.start();
    }

    private void startMoving(MotionEvent event, int index, float touchX, float touchY) {
        isMoving = true;
        directionId = MotionEventCompat.getPointerId(event, index);

        previousX = touchX;
        previousY = touchY;
    }

    private void stopPressing() {
        isPressing = false;
        positionId = -1;
        try {
            moveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stopMoving() {
        isMoving = false;
        directionId = -1;
    }

}
