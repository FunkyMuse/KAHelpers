package dev.funkymuse.recyclerview.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView




class LinearEntrust(leftRight: Int, topBottom: Int, mColor: Int) :
        SpacesItemDecorationEntrust(leftRight, topBottom, mColor) {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as LinearLayoutManager?
        if (mDivider == null || layoutManager!!.childCount == 0) {
            return
        }
        var left: Int
        var right: Int
        var top: Int
        var bottom: Int
        val childCount = parent.childCount
        if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
            for (i in 0 until childCount - 1) {
                val child = parent.getChildAt(i)
                val center = (layoutManager.getTopDecorationHeight(child) + 1 - topBottom) / 2
                left = layoutManager.getLeftDecorationWidth(child)
                right = parent.width - layoutManager.getLeftDecorationWidth(child)
                top = (child.bottom + center)
                bottom = top + topBottom
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }
        } else {
            for (i in 0 until childCount - 1) {
                val child = parent.getChildAt(i)
                val center = (layoutManager.getLeftDecorationWidth(child) + 1 - leftRight) / 2
                left = (child.right + center)
                right = left + leftRight
                top = layoutManager.getTopDecorationHeight(child)
                bottom = parent.height - layoutManager.getTopDecorationHeight(child)
                mDivider?.setBounds(left, top, right, bottom)
                mDivider?.draw(c)
            }
        }

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as LinearLayoutManager?
        if (layoutManager!!.orientation == LinearLayoutManager.VERTICAL) {
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.bottom = 0
            }
            outRect.top = topBottom
            outRect.left = leftRight
            outRect.right = leftRight
        } else {
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.right = leftRight
            }
            outRect.top = topBottom
            outRect.left = leftRight
        }
    }
}
