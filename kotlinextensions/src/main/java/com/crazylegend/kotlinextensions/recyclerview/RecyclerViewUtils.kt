package com.crazylegend.kotlinextensions.recyclerview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.SparseBooleanArray
import android.view.View
import android.widget.CheckBox
import androidx.annotation.IdRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.context.getCompatDrawable
import com.google.android.material.snackbar.Snackbar
import java.util.*


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

class RecyclerViewUtils {

    var selectedPosition = -1
    private val selectedItems = SparseBooleanArray()

    fun getSelectedItemsCount(): Int {
        return selectedItems.size()
    }

    fun clearSelectedItems(){
        selectedItems.clear()
    }

    fun getSelectedItems(): SparseBooleanArray {
        return selectedItems
    }

    fun addHorizontalDivider(context: Context, recyclerView: RecyclerView, layoutManager: LinearLayoutManager) {
        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    fun <T> removeSelectedItems(list: MutableList<T>, adapter: RecyclerView.Adapter<*>) {

        for (i in selectedItems.size() - 1 downTo 0) {
            deleteItems(list, adapter, getSelectedItemsPositions()[i])
        }
        clearSelected(adapter)
    }

    private fun getSelectedItemsPositions(): List<Int> {
        val list: ArrayList<Int> = ArrayList()

        for (i in 0 until selectedItems.size()) {
            list.add(selectedItems.keyAt(i))
        }
        return list
    }


    private fun <T> deleteItems(list: MutableList<T>, adapter: RecyclerView.Adapter<*>, position: Int) {
        list.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, adapter.itemCount)
    }


    private fun clearSelected(adapter: RecyclerView.Adapter<*>) {
        adapter.notifyItemRangeChanged(selectedItems.keyAt(0), adapter.itemCount)
        selectedItems.clear()
    }

    fun clearAllSelectedOnRecycler(adapter: RecyclerView.Adapter<*>){
        adapter.notifyItemRangeChanged(selectedItems.keyAt(0), adapter.itemCount)
        selectedItems.clear()
    }


    fun selectItemsOnCheckbox(checkBox: CheckBox, adapterPosition: Int) {

        checkBox.isChecked = selectedItems.get(adapterPosition)

        checkBox.setOnClickListener {

            if (selectedItems.get(adapterPosition, false)) {
                selectedItems.delete(adapterPosition)
                checkBox.isChecked = false
            } else {
                selectedItems.put(adapterPosition, true)
                checkBox.isChecked = true
            }
        }
    }

    fun putSelectedItem(position: Int){

        selectedItems.put(position, true)

    }

    fun removeSelectedItem(position: Int){

        selectedItems.delete(position)

    }

    fun <T> insertItemOnLastPosition(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, itemToInsert: T) {
        adapterList.add(itemToInsert)
        adapter.notifyItemInserted(adapterList.size - 1)
    }

    fun <T> changeItemAtPosition(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, positionForChanging: Int, itemToChange: T) {
        adapterList[positionForChanging] = itemToChange
        adapter.notifyItemChanged(positionForChanging)
    }

    fun <T> insertMultipleItems(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, insertionStartsAt: Int, listToInsert: MutableList<T>) {
        adapterList.addAll(listToInsert)
        adapter.notifyItemRangeChanged(insertionStartsAt, listToInsert.size)
    }

    fun <T> deleteItem(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, positionForDeletion: Int) {

        adapterList.removeAt(positionForDeletion)
        adapter.notifyItemRemoved(positionForDeletion)
        adapter.notifyItemRangeChanged(positionForDeletion, adapter.itemCount)


    }

    fun <T> deleteItemWithUndo(adapterList: MutableList<T>, adapter: RecyclerView.Adapter<*>, positionForDeletion: Int, rootLayout: View) {

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

    fun <T> swipeAndDrag(adapter: RecyclerView.Adapter<*>, isLongPressEnabled:Boolean, isSwipeEnabled:Boolean,
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

    fun <T> swipeAndDrag(adapter: RecyclerView.Adapter<*>, isMovingItemsEnabled: Boolean, isSwipeEnabled: Boolean,
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

}
