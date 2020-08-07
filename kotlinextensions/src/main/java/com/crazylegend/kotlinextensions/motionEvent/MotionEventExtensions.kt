package com.crazylegend.kotlinextensions.motionEvent

import android.view.MotionEvent


/**
 * Created by crazy on 8/7/20 to long live and prosper !
 */


fun MotionEvent.isNewGesture(): Boolean {
    return action == MotionEvent.ACTION_DOWN
}