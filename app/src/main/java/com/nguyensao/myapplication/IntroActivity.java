package com.nguyensao.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        LottieAnimationView animationView = findViewById(R.id.animationIntro);
        Intent intent = getIntent();

        animationView.addAnimatorListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
            }
            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
            }
            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
            }
            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
                startActivity(new Intent(IntroActivity.this, SignInActivity.class));
                finish();

            }
        });
    }
}