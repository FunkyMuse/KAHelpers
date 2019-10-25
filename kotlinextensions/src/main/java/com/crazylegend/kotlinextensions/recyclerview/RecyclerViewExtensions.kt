package com.crazylegend.kotlinextensions.recyclerview

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.recyclerview.widget.*
import com.crazylegend.kotlinextensions.context.getCompatDrawable
import com.crazylegend.kotlinextensions.dp2px
import com.google.android.material.snackbar.Snackbar
import java.util.*


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */


val <VH> RecyclerView.Adapter<VH>.isEmpty: Boolean where VH : RecyclerView.ViewHolder
    get() = itemCount == 0

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

val RecyclerView.ViewHolder.context: Context
    get() = this.itemView.context


fun RecyclerView.disableAnimations() {
    this.itemAnimator?.addDuration = 0
    this.itemAnimator?.removeDuration = 0
    this.itemAnimator?.moveDuration = 0
    this.itemAnimator?.changeDuration = 0
}

fun RecyclerView.enableAnimations() {
    this.itemAnimator?.addDuration = 120
    this.itemAnimator?.removeDuration = 120
    this.itemAnimator?.moveDuration = 250
    this.itemAnimator?.changeDuration = 250
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

fun <T> recyclerSwipeAndDrag(adapter: RecyclerView.Adapter<*>, isLongPressEnabled: Boolean, isSwipeEnabled: Boolean,
                             adapterList: MutableList<T>, context: Context, @IdRes drawableLeft: Int, @IdRes drawableRight: Int,
                             leftAction: (swipedPosition: Int) -> Unit, rightAction: (swipedPosition: Int) -> Unit): ItemTouchHelper {
    val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun isLongPressDragEnabled(): Boolean {
            return isLongPressEnabled
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return isSwipeEnabled
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            viewHolder.itemView.setBackgroundColor(Color.WHITE)

            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(viewHolder.itemView)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            viewHolder?.itemView?.setBackgroundColor(Color.DKGRAY)
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(viewHolder?.itemView)
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

            if (source.adapterPosition < target.adapterPosition) {
                for (i in source.adapterPosition until target.adapterPosition) {
                    Collections.swap(adapterList, i, i + 1)
                }
            } else {
                for (i in source.adapterPosition downTo target.adapterPosition + 1) {
                    Collections.swap(adapterList, i, i - 1)
                }
            }
            adapter.notifyItemMoved(source.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition

            if (direction == ItemTouchHelper.LEFT) {
                leftAction(position)
            } else {
                rightAction(position)

            }
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

            val icon: Bitmap
            val p = Paint()
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                val itemView = viewHolder.itemView
                val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                val width = height / 3

                if (dX > 0) {
                    // left
                    p.color = Color.parseColor("#388E3C")
                    val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                    c.drawRect(background, p)
                    icon = BitmapFactory.decodeResource(context.resources, drawableLeft)
                    val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                    c.drawBitmap(icon, null, icon_dest, p)
                } else {
                    //right
                    p.color = Color.parseColor("#D32F2F")
                    val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    c.drawRect(background, p)
                    icon = BitmapFactory.decodeResource(context.resources, drawableRight)
                    val icon_dest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                    c.drawBitmap(icon, null, icon_dest, p)
                }

            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
}

fun <T> recyclerSwipeAndDrag(adapter: RecyclerView.Adapter<*>, isMovingItemsEnabled: Boolean, isSwipeEnabled: Boolean,
                             adapterList: MutableList<T>, context: Context, leftBackgroundColor: Int, drawableLeft: Int, rightBackgroundColor: Int, drawableRight: Int,
                             leftAction: (swipedPosition: Int) -> Unit, rightAction: (swipedPosition: Int) -> Unit, resetBackgroundColor: Int = 0, selectedItemBackgroundColor: Int = 0): ItemTouchHelper {
    val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun isLongPressDragEnabled(): Boolean {
            return isMovingItemsEnabled
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return isSwipeEnabled
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            if (resetBackgroundColor == 0) {
                viewHolder.itemView.setBackgroundColor(Color.WHITE)
            } else {
                viewHolder.itemView.setBackgroundColor(resetBackgroundColor)
            }

            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(viewHolder.itemView)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (selectedItemBackgroundColor == 0) {
                viewHolder?.itemView?.setBackgroundColor(Color.LTGRAY)
            } else {
                viewHolder?.itemView?.setBackgroundColor(selectedItemBackgroundColor)
            }
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(viewHolder?.itemView)
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

            if (source.adapterPosition < target.adapterPosition) {
                for (i in source.adapterPosition until target.adapterPosition) {
                    Collections.swap(adapterList, i, i + 1)
                }
            } else {
                for (i in source.adapterPosition downTo target.adapterPosition + 1) {
                    Collections.swap(adapterList, i, i - 1)
                }
            }
            adapter.notifyItemMoved(source.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition

            if (direction == ItemTouchHelper.LEFT) {
                leftAction(position)
            } else {
                rightAction(position)

            }
        }

        val leftColor = ColorDrawable(leftBackgroundColor)
        val rightColor = ColorDrawable(rightBackgroundColor)
        val leftIcon = context.getCompatDrawable(drawableLeft)
        val rightIcon = context.getCompatDrawable(drawableRight)


        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {


            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                val itemView = viewHolder.itemView
                val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                val width = height / 3

                if (dX > 0) {
                    // left

                    leftColor.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    leftColor.draw(c)

                    leftIcon?.setBounds((itemView.left + width).toInt(),
                            (itemView.top + width).toInt(), (itemView.left + 2 * width).toInt(), (itemView.bottom - width).toInt()
                    )
                    leftIcon?.draw(c)

                } else {
                    //right

                    rightColor.setBounds((itemView.right + dX).toInt(), itemView.top, itemView.right, itemView.bottom)
                    rightColor.draw(c)

                    rightIcon?.setBounds((itemView.right - 2 * width).toInt(),
                            (itemView.top + width).toInt(), (itemView.right - width).toInt(), (itemView.bottom - width).toInt()
                    )

                    rightIcon?.draw(c)
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
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
    spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
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
    onScrollStateChanged :(recycler:RecyclerView, newState:Int) -> Unit = {_,_->},
    onScrolled :(recycler:RecyclerView, scrollbyX:Int, scrollbyY:Int) -> Unit = {_,_,_->}
){
    this.addOnScrollListener(object: RecyclerView.OnScrollListener() {
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