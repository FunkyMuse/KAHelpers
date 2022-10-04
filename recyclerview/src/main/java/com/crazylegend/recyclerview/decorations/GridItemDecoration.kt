package com.crazylegend.recyclerview.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */
open class GridItemDecoration : RecyclerView.ItemDecoration() {
    var gridSpacingPx: Int = 0
    var gridSize: Int = 0
    private var needSpacing = false

    fun getPortraitItemOffsets(outRect: Rect, view: View, baseEdge: Int) {
        val length = (baseEdge - gridSpacingPx.toFloat().times(gridSize.minus(1))).div(gridSize).toInt()
        val padding = baseEdge.div(gridSize).minus(length)
        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).bindingAdapterPosition

        outRect.top = if (itemPosition < gridSize) 0 else gridSpacingPx
        outRect.bottom = 0

        when {
            itemPosition.rem(gridSize) == 0 -> {
                needSpacing = true
                outRect.left = 0
                outRect.right = padding
            }
            itemPosition.plus(1).rem(gridSize) == 0 -> {
                needSpacing = false
                outRect.left = padding
                outRect.right = 0
            }
            needSpacing -> {
                needSpacing = false
                outRect.left = gridSpacingPx - padding
                if (itemPosition.plus(2).rem(gridSize) == 0) {
                    outRect.right = gridSpacingPx.minus(padding)
                } else {
                    outRect.right = gridSpacingPx.div(2)
                }
            }
            itemPosition.plus(2).rem(gridSize) == 0 -> {
                needSpacing = false
                outRect.left = gridSpacingPx.div(2)
                outRect.right = gridSpacingPx.minus(padding)
            }
            else -> {
                needSpacing = false
                outRect.left = gridSpacingPx.div(2)
                outRect.right = gridSpacingPx.div(2)
            }
        }
    }

    fun getLandScapeItemOffsets(outRect: Rect, view: View, baseEdge: Int) {
        val transpose = Rect().apply {
            top = outRect.left
            bottom = outRect.right
            left = outRect.top
            right = outRect.bottom
        }

        getPortraitItemOffsets(transpose, view, baseEdge)

        with(outRect) {
            left = transpose.top
            right = transpose.bottom
            top = transpose.left
            bottom = transpose.right
        }
    }
}