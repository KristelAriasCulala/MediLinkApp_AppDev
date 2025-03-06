package com.example.medilink_app;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView arrowImageView;
    private TextView getStartedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrowImageView = findViewById(R.id.arrowImageView);
        getStartedText = findViewById(R.id.getStartedText);

        arrowImageView.setOnTouchListener(new SwipeGestureDetector(this, arrowImageView) {
            @Override
            public void onSwipeRight() {
                // Convert 250dp to pixels
                float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());

                // Animate the arrow
                ObjectAnimator animator = ObjectAnimator.ofFloat(arrowImageView, "translationX", 0f, distance);
                animator.setDuration(300);
                animator.start();

                animator.addListener(new android.animation.AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        getStartedText.setVisibility(TextView.INVISIBLE);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset the arrow's position and make the "Get Started" text visible
        arrowImageView.setTranslationX(0f);
        getStartedText.setVisibility(TextView.VISIBLE);
    }
}