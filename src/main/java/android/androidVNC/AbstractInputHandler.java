package android.androidVNC;

import android.view.KeyEvent;
import android.view.MotionEvent;

interface AbstractInputHandler {
    CharSequence getHandlerDescription();

    String getName();

    boolean onKeyDown(int i, KeyEvent keyEvent);

    boolean onKeyUp(int i, KeyEvent keyEvent);

    boolean onTouchEvent(MotionEvent motionEvent);

    boolean onTrackballEvent(MotionEvent motionEvent);
}
