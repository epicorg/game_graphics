package game.listeners;

import android.util.Log;
import android.view.MotionEvent;

import game.controls.Button;
import game.controls.ButtonsControl;

/**
 * Performs direction motion actions and other actions given by the {@link Button} of a {@link ButtonsControl}.
 *
 * @author Torlaschi
 */
public class TouchListener implements TouchListenerInterface {

    public static final String LOG_TAG = "TouchListener";


    private ButtonsControl buttonsControl;
    private DirectionMoveListenerInterface directionMoveListener;

    private int positionId = -1;
    private volatile boolean isPressing = false;
    private int directionId = -1;
    private volatile boolean isMoving = false;

    private float previousX, previousY;
    private Thread moveThread;

    private boolean readyToPlay = false;
    private long time_sleep;

    /**
     * Creates a new <code>TouchListener</code>.
     *
     * @param buttonsControl        Control that contains the <code>Button</code> which <code>ButtonAction</code> are invoked when the listener detectes the pressing.
     * @param directionMoveListener listener for direction move actions on the screen.
     * @param time_sleep            time that indicates how long the pressed <code>Button</code> remains pressed, for <code>Button</code> that allow continuous pressing.
     */
    public TouchListener(ButtonsControl buttonsControl, DirectionMoveListenerInterface directionMoveListener, int time_sleep) {
        this.buttonsControl = buttonsControl;
        this.directionMoveListener = directionMoveListener;
        this.time_sleep = (long) time_sleep;
    }

    public void setReadyToPlay(boolean readyToPlay) {
        this.readyToPlay = readyToPlay;
    }

    @Override
    public void onTouchEvent(final MotionEvent event) {
        final int action = event.getActionMasked();
        final int index = event.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                final float touchX = event.getX(index);
                final float touchY = event.getY(index);

                if (buttonsControl.isInsideAButton(touchX, touchY) && !isPressing) {
                    startPressing(event, index, touchX, touchY);
                } else if (!isMoving) {
                    startMoving(event, index, touchX, touchY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (isPressing && (event.getPointerId(index) == positionId)) {
                    stopPressing();
                } else if (isMoving && (event.getPointerId(index) == directionId)) {
                    stopMoving();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!readyToPlay)
                    return;

                actionMovePositionProcessor(event);
                actionMoveDirectionProcessor(event);
                break;
        }
    }

    private void actionMovePositionProcessor(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {
            if (event.getPointerId(i) == positionId && isPressing) {
                final float touchX = event.getX(i);
                final float touchY = event.getY(i);

                if (!buttonsControl.isInsideAButton(touchX, touchY)) {
                    stopPressing();
                }
            } else if (event.getPointerId(i) != directionId && !isPressing) {
                final float touchX = event.getX(i);
                final float touchY = event.getY(i);

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
                    final float touchX = event.getX(i);
                    final float touchY = event.getY(i);

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

    private void startPressing(MotionEvent event, int index, final float touchX, final float touchY) {
        isPressing = true;
        positionId = event.getPointerId(index);

        moveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isPressing) {
                    Button pressedButton = buttonsControl.getPressedButton(touchX, touchY);

                    if (pressedButton.isNeedToBeReady() && !readyToPlay) {
                        Log.d(LOG_TAG, "Not readyToPlay.");
                        return;
                    }

                    //Log.d(LOG_TAG, "Pressed " + pressedButton.getName() + " button.");
                    pressedButton.execute(time_sleep);

                    if (!pressedButton.isContinuousPressing())
                        return;

                    try {
                        Thread.sleep(time_sleep);
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
        directionId = event.getPointerId(index);

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
