package com.funkymuse.customviews

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.os.bundleOf


class StatefulMotionLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val PROGRESS_STATE = "progressState"
        private const val SUPER_STATE = "superState"
    }

    override fun onSaveInstanceState() =
            bundleOf(Pair(SUPER_STATE, super.onSaveInstanceState()), Pair(PROGRESS_STATE, progress))

    override fun onRestoreInstanceState(state: Parcelable?) {
        var restoredState = state
        if (restoredState is Bundle) {
            progress = restoredState.getFloat(PROGRESS_STATE, 0f)
            restoredState = restoredState.getParcelable(SUPER_STATE)
        }
        super.onRestoreInstanceState(restoredState)
    }
}