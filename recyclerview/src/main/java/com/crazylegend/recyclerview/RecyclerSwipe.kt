package com.crazylegend.recyclerview

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by crazy on 1/30/20 to long live and prosper !
 */

data class RecyclerSwipeItemHandler(var drawableLeft: Int? = null,
                                    var drawLeftBackground: Boolean = false,
                                    var leftBackgroundColor: Int? = null,

                                    var drawableRight: Int? = null,
                                    var drawRightBackground: Boolean = false,
                                    var rightBackgroundColor: Int? = null,

                                    var swipeDirection: SwipeDirs = SwipeDirs.BOTH
) {

    enum class SwipeDirs {
        LEFT_ONLY,
        RIGHT_ONLY,
        BOTH;
    }

    operator fun invoke(recyclerSwipeItemHandler: RecyclerSwipeItemHandler.() -> Unit) {
        recyclerSwipeItemHandler.invoke(this)
    }
}

private fun swipeDirection(swipeDirection: RecyclerSwipeItemHandler.SwipeDirs): Int {
    return when (swipeDirection) {
        RecyclerSwipeItemHandler.SwipeDirs.LEFT_ONLY -> ItemTouchHelper.START
        RecyclerSwipeItemHandler.SwipeDirs.RIGHT_ONLY -> ItemTouchHelper.END
        RecyclerSwipeItemHandler.SwipeDirs.BOTH -> ItemTouchHelper.START or ItemTouchHelper.END
    }
}

/**
 * Don't forget to call [ItemTouchHelper.attachToRecyclerView] on this since it returns [ItemTouchHelper]
 * or use [addSwipe]
 * @param context Context
 * @param leftAction callback of the left swiped position
 * @param rightAction callback of the right swiped position
 * @param recyclerSwipeItemHandler callback to setting things up
 * @return ItemTouchHelper
 */
fun recyclerSwipe(context: Context,
                  leftAction: (swipedPosition: Int) -> Unit = { _ -> },
                  rightAction: (swipedPosition: Int) -> Unit = { _ -> },
                  recyclerSwipeItemHandler: RecyclerSwipeItemHandler.() -> Unit): ItemTouchHelper {

    val handler = RecyclerSwipeItemHandler()
    recyclerSwipeItemHandler.invoke(handler)

    val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(viewHolder.itemView)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(viewHolder?.itemView)
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            return ItemTouchHelper.Callback.makeMovementFlags(0, swipeDirection(handler.swipeDirection))
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

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
                    if (handler.drawLeftBackground) {
                        handler.leftBackgroundColor?.let {
                            p.color = ContextCompat.getColor(context, it)
                        }
                        val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        c.drawRect(background, p)
                    }

                    handler.drawableLeft?.let { leftDrawable ->
                        icon = BitmapFactory.decodeResource(context.resources, leftDrawable)
                        val iconDest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, iconDest, p)
                    }

                } else {
                    //right
                    if (handler.drawRightBackground) {
                        handler.rightBackgroundColor?.let {
                            p.color = ContextCompat.getColor(context, it)
                        }
                        val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        c.drawRect(background, p)
                    }

                    handler.drawableRight?.let { rightDrawable ->
                        icon = BitmapFactory.decodeResource(context.resources, rightDrawable)
                        val iconDest = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                        c.drawBitmap(icon, null, iconDest, p)
                    }
                }

            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
}


/**
 * Extension function for [recyclerSwipe]
 * @receiver RecyclerView
 * @param context we're passing separate context due to [RecyclerView] sometimes not providing context, weird I know
 * @param leftAction Function1<[@kotlin.ParameterName] Int, Unit>
 * @param rightAction Function1<[@kotlin.ParameterName] Int, Unit>
 * @param recyclerSwipeItemHandler [@kotlin.ExtensionFunctionType] Function1<RecyclerSwipeItemHandler, Unit>
 */
fun RecyclerView?.addSwipe(context: Context, leftAction: (swipedPosition: Int) -> Unit = { _ -> },
                           rightAction: (swipedPosition: Int) -> Unit = { _ -> },
                           recyclerSwipeItemHandler: RecyclerSwipeItemHandler.() -> Unit) {
    recyclerSwipe(context, leftAction, rightAction, recyclerSwipeItemHandler).attachToRecyclerView(this)
}