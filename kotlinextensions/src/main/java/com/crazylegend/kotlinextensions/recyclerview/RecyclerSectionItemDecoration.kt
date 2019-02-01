package com.crazylegend.kotlinextensions.recyclerview

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
class RecyclerSectionItemDecoration(
    private val headerOffset: Int,
    private val sticky: Boolean,
    private val sectionCallback: SectionCallback,
    @IdRes private val headerViewID: Int,
    @IdRes private val sectionHeaderLayoutName: Int
) : RecyclerView.ItemDecoration() {

    private var headerView: View? = null
    private var header: TextView? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val pos = parent.getChildAdapterPosition(view)
        if (sectionCallback.isSection(pos)) {
            outRect.top = headerOffset
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        if (headerView == null) {
            headerView = inflateHeaderView(parent)
            header = headerView!!.findViewById(headerViewID) //R.id.list_item_section_text
            fixLayoutSize(
                headerView!!,
                parent
            )
        }

        var previousHeader: CharSequence = ""
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            val title = sectionCallback.getSectionHeader(position)
            header!!.text = title
            if (previousHeader != title || sectionCallback.isSection(position)) {
                drawHeader(c, child, headerView)
                previousHeader = title
            }
        }
    }

    private fun drawHeader(c: Canvas, child: View, headerView: View?) {
        c.save()
        if (sticky) {
            c.translate(
                0f, Math.max(0, child.top - headerView!!.height).toFloat()
            )
        } else {
            c.translate(0f, (child.top - headerView!!.height).toFloat())
        }
        headerView.draw(c)
        c.restore()
    }

    //sectionheaderlayoutName e.g R.layout.section_header
    private fun inflateHeaderView(parent: RecyclerView): View {
        return LayoutInflater.from(parent.context)
            .inflate(
                sectionHeaderLayoutName,
                parent,
                false
            )
    }


    private fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.UNSPECIFIED
        )

        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    interface SectionCallback {

        fun isSection(position: Int): Boolean

        fun getSectionHeader(position: Int): CharSequence
    }
}

/*
 val sectionItemDecoration = RecyclerSectionItemDecoration(resources.getDimensionPixelSize(R.dimen.recycler_section_header_height),
            false, // true for sticky, false for not, R.id.list_item_section_text, R.layout.section_header
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