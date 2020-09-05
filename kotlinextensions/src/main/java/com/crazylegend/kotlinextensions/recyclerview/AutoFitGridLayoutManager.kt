package com.crazylegend.kotlinextensions.recyclerview

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max


/**
 * Created by crazy on 2019-06-23 to long live and prosper !
 */

/**
 *         gridLayoutManager = AutoFitGridLayoutManager(requireActivity(), COLUMN_WIDTH) // column_width = 500

 *  layout width must be match parent
 *
 * <?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView xmlns:android="http://schemas.android.com/apk/res/android"
android:layout_width="match_parent"
app:cardCornerRadius="@dimen/materialCorner"
app:cardUseCompatPadding="true"
android:layout_margin="5dp"
android:id="@+id/cardItself"
xmlns:app="http://schemas.android.com/apk/res-auto"
android:layout_height="220dp">

<RelativeLayout
android:layout_width="match_parent"
android:layout_height="match_parent">

<androidx.appcompat.widget.AppCompatImageView
android:layout_width="match_parent"
android:scaleType="fitXY"
android:id="@+id/iv_movieImage"
android:layout_height="match_parent" />

<com.crazylegend.customviews.ui.ShadowView
android:layout_width="match_parent"
android:layout_alignParentBottom="true"
app:startColor="@color/cardview_dark_background"
android:layout_height="50dp" />

<com.google.android.material.textview.MaterialTextView
android:layout_width="match_parent"
android:padding="5dp"
android:id="@+id/iv_movieTitle"
android:text="asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfadsfadsfasdf"
android:layout_alignParentBottom="true"
android:layout_height="50dp" />

</RelativeLayout>


</com.google.android.material.card.MaterialCardView>
 *
 *
 *
 */

class AutoFitGridLayoutManager(context: Context, columnWidth: Int) : GridLayoutManager(context, 1) {

    private var columnWidth: Int = 0
    private var columnWidthChanged = true

    init {

        setColumnWidth(columnWidth)
    }

    private fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth
            columnWidthChanged = true
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        if (columnWidthChanged && columnWidth > 0) {
            val totalSpace: Int = if (orientation == LinearLayoutManager.VERTICAL) {
                width - paddingRight - paddingLeft
            } else {
                height - paddingTop - paddingBottom
            }
            val spanCount = max(1, totalSpace / columnWidth)
            setSpanCount(spanCount)
            columnWidthChanged = false
        }
        super.onLayoutChildren(recycler, state)
    }
}