package com.funkymuse.recyclerview

import android.content.Context
import android.util.SparseBooleanArray
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.RecyclerView




class RecyclerViewUtilsActionMode {

    private var started = false

    fun isStarted(): Boolean {
        return started
    }

    fun finish(adapter: RecyclerView.Adapter<*>, context: Context) {
        started = false
        clearSelected(adapter)
        (context as AppCompatActivity).supportActionBar?.show()
    }

    fun finish(adapter: RecyclerView.Adapter<*>, context: Context, showSupportBar: Boolean) {
        started = false
        clearSelected(adapter)
        if (showSupportBar)
            (context as AppCompatActivity).supportActionBar?.show()
    }

    fun getValueForPosition(adapterPosition: Int): Boolean {
        return selectedItems.get(adapterPosition, false)
    }

    fun deleteValueAtPosition(adapterPosition: Int) {
        selectedItems.delete(adapterPosition)
    }

    fun insertValueAtPosition(adapterPosition: Int, value: Boolean) {
        selectedItems.put(adapterPosition, value)
    }

    var menuClicker: MenuClicker? = null

    private var selectedItems = SparseBooleanArray()


    fun setupActionMode(context: Context, menuID: Int, startingPosition: Int, adapter: RecyclerView.Adapter<*>): ActionMode? {

        started = startingPosition >= 0

        var actionMode: ActionMode?

        val actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

                item?.itemId?.apply {
                    menuClicker?.menuID(this)?.apply {
                        return true
                    }
                }

                return false

            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(menuID, menu)
                (context as AppCompatActivity).supportActionBar?.hide()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                actionMode = null
                clearSelected(adapter)
                started = false
                adapter.notifyDataSetChanged()
                (context as AppCompatActivity).supportActionBar?.show()
            }
        }


        if (started) {
            actionMode = (context as AppCompatActivity).startSupportActionMode(actionModeCallback)
            selectedItems.put(startingPosition, true)
            adapter.notifyDataSetChanged()

        } else {
            actionMode = null
        }

        return actionMode

    }


    fun getSelectedItemsCount(): Int {
        return selectedItems.size()
    }

    fun getSelectedItems(): SparseBooleanArray {
        return selectedItems
    }

    fun clearAllSelectedOnRecycler(adapter: RecyclerView.Adapter<*>) {
        adapter.notifyItemRangeChanged(selectedItems.keyAt(0), adapter.itemCount)
        selectedItems.clear()
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


}


interface OnStartActionMode {
    fun start(atPosition: Int)
}

interface MenuClicker {
    fun menuID(itemId: Int): Boolean
}

/* // activity

 companion object {
        val recyclerViewUtilsActionMode = RecyclerViewUtilsActionMode()
    }

 recyclerView.adapter = customAdapter
        var actionMode: ActionMode? = null
        customAdapter.onStartActionMode = object : OnStartActionMode {
            override fun start(atPosition: Int) {
                actionMode = recyclerViewUtilsActionMode.setupActionMode(activity, R.menu.templates_action_menu, atPosition, customAdapter)
                //(requireActivity() as AppCompatActivity).supportActionBar?.hide()
            }
        }

        recyclerViewUtilsActionMode.menuClicker = object : MenuClicker {
            override fun menuID(itemId: Int): Boolean {
                return when (itemId) {

                    R.id.something -> {

                        someAction()


                            val selectedItems = recyclerViewUtilsActionMode.getSelectedItems()

                           // delete(list, selectedItems)

                            actionMode?.finish()
                            recyclerViewUtilsActionMode.finish(customAdapter, activity())

                        true
                    }

                    else -> {

                        true
                    }
                }
            }
        }*/

/*
//inside adapter

var onStartActionMode: OnStartActionMode? = null

  if (!YourActivity.recyclerViewUtilsActionMode.isStarted()) {
            holder.itemView.setOnLongClickListener {

                onStartActionMode?.start(position)

                true
            }
        } else {

            holder.itemView.setOnLongClickListener(null)

        }

        if (YourActivity.recyclerViewUtilsActionMode.getValueForPosition(position)) {
            holder.templateImage.setImageDrawable(context.getDrawable(R.drawable.selected_round))

        } else {

            setupImage(item, holder)

        }

        if (YourActivity.recyclerViewUtilsActionMode.isStarted()) {
            holder.itemView.setOnClickListener {
                if (YourActivity.recyclerViewUtilsActionMode.getValueForPosition(position)) {
                    YourActivity.recyclerViewUtilsActionMode.deleteValueAtPosition(position)

                    setupImage(item, holder)

                } else {
                    YourActivity.recyclerViewUtilsActionMode.insertValueAtPosition(position, true)


                    holder.templateImage.setImageBitmap(null)
                    holder.templateImage.setImageDrawable(context.getDrawable(R.drawable.selected_round))
                }
            }
        } else {
            holder.itemView.setOnClickListener {
                onItemClickListener.onItemClicked(it, position)
            }
        }*/
