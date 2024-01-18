package com.funkymuse.recyclerview

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

/**
 * /*   USAGE
val sectionItemDecoration = RecyclerSectionItemDecoration(R.id.list_item_section_text, R.layout.section_header
object : RecyclerSectionItemDecoration.SectionCallback {

override fun isSection(position: Int): Boolean {
return position == 0 || list[position].name[0] != list[position-1].surname[0]
}

override fun getSectionHeader(position: Int): CharSequence {
return list[position].name.subSequence(0, 1)
}
})
recyclerView.addItemDecoration(sectionItemDecoration)
*/
/*

<TextView xmlns:android="http://schemas.android.com/apk/res/android"
android:id="@+id/list_item_section_text"
android:layout_width="match_parent"
android:layout_height="@dimen/recycler_section_header_height"
android:background="@android:color/black"
android:paddingLeft="10dp"
android:paddingRight="10dp"
android:textColor="@android:color/white"
android:textSize="14sp"
/>*/
 */
class RecyclerHorizontalItemHeaderDecoration(
        private val sectionCallback: SectionCallback,
        private val headerViewID: Int,
        private val sectionHeaderLayoutName: Int
) : RecyclerView.ItemDecoration() {

    private var headerView: View? = null
    private var header: MaterialTextView? = null

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (headerView == null) {
            headerView = inflateHeaderView(parent)
            header = headerView?.findViewById(headerViewID) //R.id.list_item_section_text
            headerView?.let { fixLayoutSize(it, parent) }
        }
        var previousHeader: CharSequence = ""
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val title = sectionCallback.getSectionHeader(position)
            header?.text = title
            if (previousHeader != title || sectionCallback.isSection(position)) {
                header?.draw(c)
                previousHeader = title
            }
        }
    }

    //sectionheaderlayoutName e.g R.layout.section_header
    private fun inflateHeaderView(parent: RecyclerView): View =
            LayoutInflater.from(parent.context).inflate(sectionHeaderLayoutName, parent, false)

    private fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
                parent.width,
                View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
                parent.height,
                View.MeasureSpec.UNSPECIFIED
        )
        view.measure(widthSpec, heightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    interface SectionCallback {
        fun isSection(position: Int): Boolean
        fun getSectionHeader(position: Int): CharSequence
    }
}