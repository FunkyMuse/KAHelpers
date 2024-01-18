package com.funkymuse.recyclerview.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView



class PortraitGridItemDecoration : GridItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        getPortraitItemOffsets(outRect, view, parent.height)
    }
}