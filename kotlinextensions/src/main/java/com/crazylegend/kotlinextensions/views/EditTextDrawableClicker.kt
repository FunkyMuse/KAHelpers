package com.crazylegend.kotlinextensions.views

import android.view.MotionEvent
import android.widget.EditText


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
class EditTextDrawableClicker {
    companion object {
        const val DRAWABLE_LEFT = 0
        const val DRAWABLE_TOP = 1
        const val DRAWABLE_RIGHT = 2
        const val DRAWABLE_BOTTOM = 3
    }

    fun clickListener(event: MotionEvent, editText: EditText, drawablePosition: Int, action: () -> Unit) {

        val positions = arrayOf(
            DRAWABLE_LEFT,
            DRAWABLE_TOP,
            DRAWABLE_RIGHT,
            DRAWABLE_BOTTOM
        )

        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (editText.right - editText.compoundDrawables[positions[drawablePosition]].bounds.width())) {
                action()
            }
        }

    }
}
