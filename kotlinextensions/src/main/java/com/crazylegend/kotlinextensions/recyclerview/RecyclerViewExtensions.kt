package com.crazylegend.kotlinextensions.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.*
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.context.getCompatDrawable
import com.crazylegend.kotlinextensions.dp2px
import com.google.android.material.snackbar.Snackbar


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */


val <VH> RecyclerView.Adapter<VH>.isEmpty: Boolean where VH : RecyclerView.ViewHolder
    get() = itemCount == 0


val <VH> RecyclerView.Adapter<VH>.isNotEmpty: Boolean where VH : RecyclerView.ViewHolder
    get() = !isEmpty

/**
 * Disable all user input to a recyclerview, passing touch events out
 */
fun RecyclerView.disableTouch() {
    this.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return true
        }
    })
}



fun RecyclerView.divider(color: Int = Color.parseColor("#CCCCCC"), size: Int = 1): RecyclerView {
    val decoration = DividerItemDecoration(context, orientation)
    decoration.setDrawable(GradientDrawable().apply {
        setColor(color)
        shape = GradientDrawable.RECTANGLE
        val dpSize = dp2px(size)
        dpSize?.let {
            setSize(dpSize, dpSize)
        }
    })
    addItemDecoration(decoration)
    return this
}

fun RecyclerView.ViewHolder.getString(resId: Int) = context.getString(resId)
fun RecyclerView.ViewHolder.getString(resId: Int, format: Any) = context.getString(resId, format)
fun RecyclerView.ViewHolder.getDrawable(resId: Int) = context.getCompatDrawable(resId)
fun RecyclerView.ViewHolder.getColor(resId: Int) = context.getCompatColor(resId)


val RecyclerView.ViewHolder.context: Context
    get() = this.itemView.context


fun RecyclerView.disableAnimations() {
    this.itemAnimator?.addDuration = 0
    this.itemAnimator?.removeDuration = 0
    this.itemAnimator?.moveDuration = 0
    this.itemAnimator?.changeDuration = 0
}

fun RecyclerView.enableAnimations(addDuration: Long = 120, removeDuration: Long = 120, moveDuration: Long = 250, changeDuration: Long = 250) {
    this.itemAnimator?.addDuration = addDuration
    this.itemAnimator?.removeDuration = removeDuration
    this.itemAnimator?.moveDuration = moveDuration
    this.itemAnimator?.changeDuration = changeDuration
}


fun RecyclerView.vertical(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    }
    return this
}

fun RecyclerView.horizontal(spanCount: Int = 0, isStaggered: Boolean = false): RecyclerView {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
    }
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    }
    return this
}

inline val RecyclerView.orientation
    get() = if (layoutManager == null) -1 else layoutManager.run {
        when (this) {
            is LinearLayoutManager -> orientation
            is GridLayoutManager -> orientation
            is StaggeredGridLayoutManager -> orientation
            else -> -1
        }
    }


/**
 * Generates a recycler view with match parent and a linearlayoutmanager, since it's so commonly used
 */
fun Context.fullLinearRecycler(rvAdapter: RecyclerView.Adapter<*>? = null, configs: RecyclerView.() -> Unit = {}) =
        RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@fullLinearRecycler)
            layoutParams =
                    RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT)
            if (rvAdapter != null) adapter = rvAdapter
            configs()
        }

/**
 * Sets a linear layout manager along with an adapter
 */
fun RecyclerView.withLinearAdapter(rvAdapter: RecyclerView.Adapter<*>) = apply {
    layoutManager = LinearLayoutManager(context)
    adapter = rvAdapter
}


fun RecyclerView.addHorizontalDivider(context: Context, layoutManager: LinearLayoutManager) {
    val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
    this.addItemDecoration(dividerItemDecoration)
}


fun <T> recyclerDeleteItems(list: MutableList<T>, adapter: RecyclerView.Adapter<*>, position: Int) {
    list.removeAt(position)
    adapter.notifyItemRemoved(position)
    adapter.notifyItemRangeChanged(position, adapter.itemCount)
}

fun <T> recyclerInsertItemOnLastPosition(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, itemToInsert: T) {
    adapterList.add(itemToInsert)
    adapter.notifyItemInserted(adapterList.size - 1)
}

fun <T> recyclerChangeItemAtPosition(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, positionForChanging: Int, itemToChange: T) {
    adapterList[positionForChanging] = itemToChange
    adapter.notifyItemChanged(positionForChanging)
}

fun <T> recyclerInsertMultipleItems(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, insertionStartsAt: Int, listToInsert: MutableList<T>) {
    adapterList.addAll(listToInsert)
    adapter.notifyItemRangeChanged(insertionStartsAt, listToInsert.size)
}

fun <T> recyclerDeleteItem(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, positionForDeletion: Int) {

    adapterList.removeAt(positionForDeletion)
    adapter.notifyItemRemoved(positionForDeletion)
    adapter.notifyItemRangeChanged(positionForDeletion, adapter.itemCount)


}

fun <T> recyclerDeleteItemWithUndo(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, positionForDeletion: Int, rootLayout: View) {

    //implementation 'com.google.android.material:material:1.1.0'

    val deleted = adapterList.removeAt(positionForDeletion)
    adapter.notifyItemRemoved(positionForDeletion)
    adapter.notifyItemRangeChanged(positionForDeletion, adapter.itemCount)


    Snackbar.make(rootLayout, "Item removed !", Snackbar.LENGTH_LONG)
            .setAction("Undo") {

                adapterList.add(positionForDeletion, deleted)
                adapter.notifyItemInserted(positionForDeletion)

            }.show()

}

/**
 * Adds an [RecyclerView.ItemDecoration] that draws a horizontal divider between items.
 */
fun RecyclerView.horizontalDivider(drawable: Drawable, dividerSize: Int = drawable.intrinsicHeight.coerceAtLeast(1), padding: Int = 0) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft + padding
            val right = parent.width - parent.paddingRight - padding

            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)

                drawable.mutate().alpha = (child.alpha * 255).toInt()

                val top = with(child) {
                    top - (
                            (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin ?: 0
                            ) + (translationY + .5f).toInt()
                } - dividerSize

                if (i != 0) {
                    drawable.setBounds(left, top, right, top + dividerSize)
                    drawable.draw(c)
                }

                val bottom = with(child) {
                    bottom + (
                            (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin ?: 0
                            ) + (translationY + .5f).toInt()
                }

                if (i != parent.childCount - 1) {
                    drawable.setBounds(left, bottom, right, bottom + dividerSize)
                    drawable.draw(c)
                }
            }
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(0, 0, 0, dividerSize)
        }
    })
}

/**
 * Adds an [RecyclerView.ItemDecoration] that draws a vertical divider between items.
 */
fun RecyclerView.verticalDivider(drawable: Drawable, dividerSize: Int = drawable.intrinsicHeight.coerceAtLeast(1), padding: Int = 0) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val top = parent.paddingTop + padding
            val bottom = parent.height - parent.paddingBottom - padding

            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)

                drawable.mutate().alpha = (child.alpha * 255).toInt()

                val left = with(child) {
                    left - (
                            (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin ?: 0
                            ) + (translationX + .5f).toInt()
                } - dividerSize

                if (i != 0) {
                    drawable.setBounds(left, top, right, top + dividerSize)
                    drawable.draw(c)
                }

                val right = with(child) {
                    right + (
                            (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin ?: 0
                            ) + (translationX + .5f).toInt()
                }

                if (i != parent.childCount - 1) {
                    drawable.setBounds(right, top, right + dividerSize, bottom)
                    drawable.draw(c)
                }
            }
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.set(0, 0, 0, dividerSize)
        }
    })
}

fun RecyclerView.smoothScrollTo(position: Int, callback: (() -> Unit)? = null) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                callback?.invoke()
                removeOnScrollListener(this)
            }
        }
    })
    smoothScrollToPosition(position)
}

fun GridLayoutManager.setSpanSize(func: (Int) -> Int) {
    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) = func(position)
    }
}


fun RecyclerView.isReverseLayout(): Boolean {
    return this.layoutManager?.isReverseLayout() ?: false
}

fun RecyclerView.LayoutManager.isReverseLayout(): Boolean {
    if (this is LinearLayoutManager) {
        return this.reverseLayout
    }
    return false
}

fun RecyclerView.getFirstVisibleItemPosition(): Int? {
    return layoutManager?.let { manager ->
        return when (manager) {
            is LinearLayoutManager -> manager.findFirstVisibleItemPosition()
            is GridLayoutManager -> manager.findFirstVisibleItemPosition()
            else -> null
        }
    }
}

fun RecyclerView.getLastVisibleItemPosition(): Int? {
    return layoutManager?.let { manager ->
        return when (manager) {
            is LinearLayoutManager -> manager.findLastVisibleItemPosition()
            is GridLayoutManager -> manager.findLastVisibleItemPosition()
            else -> null
        }
    }
}


fun RecyclerView.scrollListener(
        onScrollStateChanged: (recycler: RecyclerView, newState: Int) -> Unit = { _, _ -> },
        onScrolled: (recycler: RecyclerView, scrollbyX: Int, scrollbyY: Int) -> Unit = { _, _, _ -> }
) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            onScrolled(recyclerView, dx, dy)
        }
    })
}


/**
 * Calls [RecyclerView.addItemDecoration] with [ItemOffsetDecoration] as a parameter.
 *
 * @param dimenRes [DimenRes] of the desired offset
 *
 * @return `this`
 */
fun RecyclerView.withItemOffsetDecoration(@DimenRes dimenRes: Int): RecyclerView = apply {
    addItemDecoration(ItemOffsetDecoration(context, dimenRes))
}


/**
 * Set a [GridLayoutManager] as `this` layoutManager.
 *
 * @param spanCount number of grid columns
 *
 * @return `this`
 */
fun RecyclerView.withGridLayoutManager(spanCount: Int): RecyclerView = apply {
    layoutManager = GridLayoutManager(context, spanCount)
}

/**
 * Set a [LinearLayoutManager] as `this` layoutManager.
 *
 * @param vertical whether `this` layout should be vertical, default is true
 * @param reversed whether `this` layout should be reverted, default is false
 *
 * @return `this`
 */
fun RecyclerView.withLinearLayoutManager(
        vertical: Boolean = true,
        reversed: Boolean = false
): RecyclerView = apply {
    layoutManager =
            LinearLayoutManager(context, if (vertical) RecyclerView.VERTICAL else RecyclerView.HORIZONTAL, reversed)
}

fun RecyclerView.withPagerSnapHelper(): RecyclerView = apply {
    PagerSnapHelper().attachToRecyclerView(this)
}


fun RecyclerView.isFirstChild(view: View): Boolean = getChildAdapterPosition(view) == 0

fun RecyclerView.isLastChild(view: View): Boolean = getChildAdapterPosition(view) == childCount - 1


inline val RecyclerView.ViewHolder.resources: Resources
    get() = itemView.context.resources


fun RecyclerView.clearDecorations() {
    if (itemDecorationCount > 0) {
        for (i in itemDecorationCount - 1 downTo 0) {
            removeItemDecorationAt(i)
        }
    }
}


fun RecyclerView.Adapter<*>.registerDataObserver(action: () -> Unit = {}): RecyclerView.AdapterDataObserver {

    val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            action()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            action()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            action()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            action()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            action()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            action()
        }
    }

    registerAdapterDataObserver(observer)
    return observer
}


fun RecyclerView.Adapter<*>.onChanged(action: () -> Unit = { }): RecyclerView.AdapterDataObserver {
    val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            action()
        }
    }
    registerAdapterDataObserver(observer)
    return observer
}


fun RecyclerView.Adapter<*>.onItemRangeRemoved(action: (positionStart: Int, itemCount: Int) -> Unit = { _, _ -> }): RecyclerView.AdapterDataObserver {

    val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            action(positionStart, itemCount)
        }
    }

    registerAdapterDataObserver(observer)
    return observer

}

fun RecyclerView.Adapter<*>.onItemRangeMoved(action: (fromPosition: Int, toPosition: Int, itemCount: Int) -> Unit = { _, _, _ -> }): RecyclerView.AdapterDataObserver {

    val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            action(fromPosition, toPosition, itemCount)
        }
    }

    registerAdapterDataObserver(observer)
    return observer
}


fun RecyclerView.Adapter<*>.onItemRangeInserted(action: (positionStart: Int, itemCount: Int) -> Unit = { _, _ -> }): RecyclerView.AdapterDataObserver {
    val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            action(positionStart, itemCount)
        }
    }

    registerAdapterDataObserver(observer)
    return observer
}


fun RecyclerView.Adapter<*>.onItemRangeChanged(action: (positionStart: Int, itemCount: Int) -> Unit = { _, _ -> }): RecyclerView.AdapterDataObserver {
    val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            action(positionStart, itemCount)
        }
    }

    registerAdapterDataObserver(observer)
    return observer

}


fun RecyclerView.Adapter<*>.onItemRangeChanged(action: (positionStart: Int, itemCount: Int, payload: Any?) -> Unit = { _, _, _ -> }): RecyclerView.AdapterDataObserver {
    val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            action(positionStart, itemCount, payload)
        }
    }
    registerAdapterDataObserver(observer)
    return observer
}


/**Set adapter of recyclerView
 * @param yourAdapter your adapter(must extend RecyclerView.Adapter)
 * @param layoutOrientation LinearLayoutManager orientation of adapter, default is RecyclerView.VERTICAL
 * @param fixedSize isFixed size of recyclerView, default is true*/
fun <T : RecyclerView.Adapter<*>> RecyclerView.initRecyclerViewAdapter(
        yourAdapter: T,
        layoutOrientation: Int = RecyclerView.VERTICAL,
        fixedSize: Boolean = false,
        reverseLayout: Boolean = false
) {
    apply {
        layoutManager = LinearLayoutManager(context, layoutOrientation, reverseLayout)
        adapter = yourAdapter
        setHasFixedSize(fixedSize)
    }
}


/**Set adapter of recyclerView
 * @param yourAdapter your adapter(must extend RecyclerView.Adapter)
 * @param yourLayoutManager Pass your own layout manager
 * @param fixedSize isFixed size of recyclerView, default is true*/
fun <T : RecyclerView.Adapter<*>> RecyclerView.initRecyclerViewAdapter(
        yourAdapter: T,
        yourLayoutManager: RecyclerView.LayoutManager,
        fixedSize: Boolean = false
) {
    apply {
        layoutManager = yourLayoutManager
        adapter = yourAdapter
        setHasFixedSize(fixedSize)
    }
}

/**
 * To prevent the [RecyclerView] stealing touches from the bottom sheet fragment or any other fragment that scrolls, use this function
 * If you're using other recycler views besides this one, use [OrientationAwareRecyclerView] for them
 * @receiver RecyclerView?
 */
@SuppressLint("ClickableViewAccessibility")
fun RecyclerView?.handleInsideBottomSheet() {
    this?.setOnTouchListener { v, event ->
        v.parent.requestDisallowInterceptTouchEvent(true)
        v.onTouchEvent(event)
        true
    }
}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

fun RecyclerView.getSnapPosition(snapHelper: SnapHelper): Int {
    val layoutManager = layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = snapHelper.findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}
