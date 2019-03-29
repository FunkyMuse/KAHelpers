package com.crazylegend.kotlinextensions.recyclerview.utils

import android.graphics.Canvas
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

abstract class SpacesItemDecorationEntrust(protected var leftRight: Int, protected var topBottom: Int, mColor: Int) {

    protected var mDivider: Drawable? = null

    init {
        if (mColor != 0) {
            mDivider = ColorDrawable(mColor)
        }
    }


    internal abstract fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State)

    internal abstract fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State)

}
