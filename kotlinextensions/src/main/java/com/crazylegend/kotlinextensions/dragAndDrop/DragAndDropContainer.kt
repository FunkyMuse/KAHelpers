package com.crazylegend.kotlinextensions.dragAndDrop

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * Created by crazy on 8/7/20 to long live and prosper !
 */
class DragAndDropContainer(
        context: Context,
        attrs: AttributeSet?
) : FrameLayout(context, attrs) {

    private var content: View? = null

    fun addDragAndDropListener(dragAndDropListener: DragAndDropListener) {
        setOnDragListener(dragAndDropListener)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updateChild()
    }

    fun setContent(view: View) {
        removeAllViews()
        addView(view)
        updateChild()
    }

    fun removeContent(view: View) {
        view.setOnLongClickListener(null)
        removeView(view)
        updateChild()
    }

    private fun updateChild() {
        validateChildCount()
        content = getFirstChild()
        content?.setOnLongClickListener { startDrag() }
    }

    private fun getFirstChild() = if (childCount == 1) getChildAt(0) else null

    private fun validateChildCount() = check(childCount <= 1) {
        "There should be a maximum of 1 child inside of a DragAndDropContainer, but there were $childCount"
    }

    private fun startDrag(): Boolean {
        content?.let {
            val tag = it.tag as? CharSequence
            val item = ClipData.Item(tag)
            val data = ClipData(tag, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
            val shadow = DragShadowBuilder(it)

            if (Build.VERSION.SDK_INT >= 24) {
                it.startDragAndDrop(data, shadow, it, 0)
            } else {
                it.startDrag(data, shadow, it, 0)
            }
            return true
        } ?: return false
    }
}