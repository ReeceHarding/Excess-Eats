package com.exceseats.util

import android.view.View
import android.view.animation.AnimationUtils
import com.exceseats.R

fun View.fadeIn() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
    }
}

fun View.fadeOut() {
    if (visibility != View.GONE) {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
        visibility = View.GONE
    }
}
