package com.crazylegend.kotlinextensions.recyclerview.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */
class PortraitGridItemDecoration: GridItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        getPortraitItemOffsets(outRect, view, parent.height)
    }
}