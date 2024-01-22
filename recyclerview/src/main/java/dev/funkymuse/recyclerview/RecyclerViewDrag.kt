package dev.funkymuse.recyclerview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

/**
 * Don't forget to call [ItemTouchHelper.attachToRecyclerView] on this since it returns [ItemTouchHelper]
 * or use [addDrag] as an extension function
 * @param adapter Adapter<*>
 * @param adapterList MutableList<T>
 * @return ItemTouchHelper
 */
fun <T> recyclerDrag(adapter: RecyclerView.Adapter<*>,
                     adapterList: MutableList<T>): ItemTouchHelper {
    val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun isLongPressDragEnabled() = true

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

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

    }

    return ItemTouchHelper(simpleItemTouchCallback)
}


/**
 * Extension function for [recyclerDrag]
 * @receiver RecyclerView
 * @param adapter Adapter<*>
 * @param adapterList MutableList<T>
 */
fun <T> RecyclerView.addDrag(adapter: RecyclerView.Adapter<*>, adapterList: MutableList<T>) {
    recyclerDrag(adapter, adapterList).attachToRecyclerView(this)
}