package com.crazylegend.kotlinextensions.views

import android.view.MotionEvent
import android.widget.EditText
import androidx.core.view.doOnLayout


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

    private val positions = arrayOf(
            DRAWABLE_LEFT,
            DRAWABLE_TOP,
            DRAWABLE_RIGHT,
            DRAWABLE_BOTTOM
    )

    fun clickListener(event: MotionEvent, editText: EditText, drawablePosition: Int, action: () -> Unit) {
        if (event.action == MotionEvent.ACTION_UP) {
            editText.doOnLayout {
                if (!editText.compoundDrawables.isNullOrEmpty()) {
                    val width = (editText.right - editText.compoundDrawables[positions[drawablePosition]].bounds.width())

                    if (event.rawX >= width) {
                        action()
                    }
                }
            }
        }
    }
}
