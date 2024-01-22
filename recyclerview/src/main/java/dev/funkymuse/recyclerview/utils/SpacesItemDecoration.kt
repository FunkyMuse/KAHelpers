package dev.funkymuse.recyclerview.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView




class SpacesItemDecoration(private val leftRight: Int, private val topBottom: Int) : RecyclerView.ItemDecoration() {

    private var mEntrust: SpacesItemDecorationEntrust? = null
    private var theColor: Int = 0

    constructor(leftRight: Int, topBottom: Int, mColor: Int) : this(leftRight, topBottom) {
        theColor = mColor
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mEntrust == null) {
            mEntrust = getEntrust(parent.layoutManager)
        }
        mEntrust!!.onDraw(c, parent, state)
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (mEntrust == null) {
            mEntrust = getEntrust(parent.layoutManager)
        }
        mEntrust!!.getItemOffsets(outRect, view, parent, state)
    }

    private fun getEntrust(manager: RecyclerView.LayoutManager?): SpacesItemDecorationEntrust {
        val entrust: SpacesItemDecorationEntrust?
        if (manager is GridLayoutManager) {
            entrust = GridEntrust(leftRight, topBottom, theColor)
        } else {
            entrust = LinearEntrust(leftRight, topBottom, theColor)
        }
        return entrust
    }

}
