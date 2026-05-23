package com.telo.app.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class AnimationHelper {

    // ── Fade ──────────────────────────────────────────────────

    public static void fadeIn(View view, long duration) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(new AccelerateDecelerateInterpolator())
            .start();
    }

    public static void fadeOut(View view, long duration) {
        view.animate()
            .alpha(0f)
            .setDuration(duration)
            .setInterpolator(new AccelerateDecelerateInterpolator())
            .withEndAction(() -> view.setVisibility(View.GONE))
            .start();
    }

    // ── Scale ─────────────────────────────────────────────────

    public static void scaleIn(View view) {
        view.setScaleX(0f);
        view.setScaleY(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(250)
            .setInterpolator(new AccelerateDecelerateInterpolator())
            .start();
    }

    public static void pulse(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
            view, "scaleX", 1f, 1.1f, 1f
        );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
            view, "scaleY", 1f, 1.1f, 1f
        );
        scaleX.setDuration(300);
        scaleY.setDuration(300);
        scaleX.start();
        scaleY.start();
    }

    // ── Shake (wrong PIN) ─────────────────────────────────────

    public static void shake(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(
            view, "translationX",
            0f, -20f, 20f, -15f, 15f, -10f, 10f, 0f
        );
        animator.setDuration(500);
        animator.start();
    }

    // ── Slide ─────────────────────────────────────────────────

    public static void slideUp(View view, long duration) {
        view.setTranslationY(view.getHeight());
        view.setVisibility(View.VISIBLE);
        view.animate()
            .translationY(0f)
            .setDuration(duration)
            .setInterpolator(new AccelerateDecelerateInterpolator())
            .start();
    }
}