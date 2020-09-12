package de.fklappan.app.workoutlog.common

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.widget.ImageButton

fun ImageButton.playGrowAnimation() {
    // double scale the button for 200 ms and return back to normal size
    val animScaleX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 2.3f)
    with(animScaleX) {
        repeatCount = 1
        repeatMode = ValueAnimator.REVERSE
        duration = 200
    }

    val animScaleY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 2.3f)
    with(animScaleY) {
        repeatCount = 1
        repeatMode = ValueAnimator.REVERSE
        duration = 200
    }

    val growAnimator = AnimatorSet()
    growAnimator.play(animScaleX).with(animScaleY)
    growAnimator.start()
}