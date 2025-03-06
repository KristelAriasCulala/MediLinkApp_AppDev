package com.example.medilink_app;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class SwipeGestureDetector implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private final ImageView arrowImageView;

    public SwipeGestureDetector(Context context, ImageView arrowImageView) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.arrowImageView = arrowImageView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight();
                }
            }
            return true;
        }
    }

    public void onSwipeRight() {
        // Override this method in MainActivity to handle the swipe right action
    }
}