package dev.funkymuse.kotlinextensions.motionEvent

import android.view.MotionEvent

fun MotionEvent.isNewGesture(): Boolean {
    return action == MotionEvent.ACTION_DOWN
}