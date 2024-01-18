package com.funkymuse.recyclerview.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView




class GridEntrust(leftRight: Int, topBottom: Int, mColor: Int) :
        SpacesItemDecorationEntrust(leftRight, topBottom, mColor) {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as GridLayoutManager?
        val lookup = layoutManager!!.spanSizeLookup

        if (mDivider == null || layoutManager.childCount == 0) {
            return
        }
        val spanCount = layoutManager.spanCount

        var left: Int
        var right: Int
        var top: Int
        var bottom: Int

        val childCount = parent.childCount
        if (layoutManager.orientation == GridLayoutManager.VERTICAL) {

            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val position = parent.getChildAdapterPosition(child)
                val spanSize = lookup.getSpanSize(position)
                val spanIndex = lookup.getSpanIndex(position, layoutManager.spanCount)
                val centerLeft = (layoutManager.getLeftDecorationWidth(child) + 1 - leftRight) / 2
                val centerTop = (layoutManager.getBottomDecorationHeight(child) + 1 - topBottom) / 2
                val isFirst = position + spanSize <= layoutManager.spanCount
                if (!isFirst && spanIndex == 0) {
                    left = layoutManager.getLeftDecorationWidth(child)
                    right = parent.width - layoutManager.getLeftDecorationWidth(child)

                    top = (child.top - centerTop) - topBottom
                    bottom = top + topBottom
                    mDivider?.setBounds(left, top, right, bottom)
                    mDivider?.draw(c)
                }
                val isRight = spanIndex + spanSize == spanCount
                if (!isRight) {
                    left = (child.right + centerLeft)
                    right = left + leftRight
                    top = child.top

                    if (position + spanSize - 1 >= spanCount) {
                        top -= centerTop
                    }
                    bottom = (child.bottom + centerTop)
                    mDivider?.setBounds(left, top, right, bottom)
                    mDivider?.draw(c)
                }
            }
        } else {
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val position = parent.getChildAdapterPosition(child)
                val spanSize = lookup.getSpanSize(position)
                val spanIndex = lookup.getSpanIndex(position, layoutManager.spanCount)
                val centerLeft = (layoutManager.getRightDecorationWidth(child) + 1 - leftRight) / 2
                val centerTop = (layoutManager.getTopDecorationHeight(child) + 1 - topBottom) / 2
                val isFirst = position + spanSize <= layoutManager.spanCount
                if (!isFirst && spanIndex == 0) {
                    left = (child.left - centerLeft) - leftRight
                    right = left + leftRight

                    top = layoutManager.getRightDecorationWidth(child)
                    bottom = parent.height - layoutManager.getTopDecorationHeight(child)
                    mDivider?.setBounds(left, top, right, bottom)
                    mDivider?.draw(c)
                }
                val isRight = spanIndex + spanSize == spanCount
                if (!isRight) {
                    left = child.left
                    if (position + spanSize - 1 >= spanCount) {
                        left -= centerLeft
                    }
                    right = (child.right + centerTop)

                    top = (child.bottom + centerLeft)
                    bottom = top + leftRight
                    mDivider?.setBounds(left, top, right, bottom)
                    mDivider?.draw(c)
                }
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as GridLayoutManager?
        val lp = view.layoutParams as GridLayoutManager.LayoutParams
        val childPosition = parent.getChildAdapterPosition(view)
        val spanCount = layoutManager!!.spanCount

        if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
            if (childPosition + lp.spanSize - 1 < spanCount) {
                outRect.top = topBottom
            }
            if (lp.spanIndex + lp.spanSize == spanCount) {
                outRect.right = leftRight
            }
            outRect.bottom = topBottom
            outRect.left = leftRight
        } else {
            if (childPosition + lp.spanSize - 1 < spanCount) {
                outRect.left = leftRight
            }
            if (lp.spanIndex + lp.spanSize == spanCount) {
                outRect.bottom = topBottom
            }
            outRect.right = leftRight
            outRect.top = topBottom
        }
    }


}