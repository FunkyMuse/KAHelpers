package com.funkymuse.recyclerview.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView



class LandScapeGridItemDecoration : GridItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        getLandScapeItemOffsets(outRect, view, parent.width)
    }
}