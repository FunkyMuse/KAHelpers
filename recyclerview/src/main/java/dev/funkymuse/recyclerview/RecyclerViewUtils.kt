package dev.funkymuse.recyclerview

import android.content.Context
import android.util.SparseBooleanArray
import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class RecyclerViewUtils {

    var selectedPosition = -1
    private val selectedItems = SparseBooleanArray()

    fun getSelectedItemsCount(): Int {
        return selectedItems.size()
    }

    fun clearSelectedItems() {
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

    fun clearAllSelectedOnRecycler(adapter: RecyclerView.Adapter<*>) {
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

    fun putSelectedItem(position: Int) {

        selectedItems.put(position, true)

    }

    fun removeSelectedItem(position: Int) {

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


}
