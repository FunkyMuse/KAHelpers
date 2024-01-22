package dev.funkymuse.kotlinextensions.views

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter

/**
 * Callback executed when a spinner item is selected
 * @return Am implemented `OnItemSelectedListener` interface in case the same is needed for any other operation.
 */
fun Spinner.onItemSelected(
        onNothingSelect: (parent: AdapterView<*>?) -> Unit = { _ -> },
        onItemSelect: (parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit = { _, _, _, _ -> }):
        AdapterView.OnItemSelectedListener {

    val itemSelected = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
            onNothingSelect(p0)
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            onItemSelect(p0, p1, p2, p3)
        }

    }

    onItemSelectedListener = itemSelected
    return itemSelected
}

/**
 * Sets an ArrayList of objects with an additional (but optional and which defaults to `object.toString()`)
 * string conversion method for the same.
 * @return A new `ArrayAdapter` already containing the items received by this method
 */
fun <T> Spinner.setItems(
        items: ArrayList<T>?,
        layoutResource: Int = android.R.layout.simple_spinner_dropdown_item,
        getTitle: (item: T) -> String = { a -> a.toString() }): SpinnerAdapter? {

    val finalList: ArrayList<String> = ArrayList()
    items?.forEach {
        finalList.add(getTitle(it))
    }

    val myAdapter = ArrayAdapter(this.context, layoutResource, finalList)
    adapter = myAdapter

    return adapter
}