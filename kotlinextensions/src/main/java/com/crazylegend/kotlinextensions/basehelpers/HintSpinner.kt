package com.crazylegend.kotlinextensions.basehelpers

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner

/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */
class HintSpinner(context: Context, mode: Int, val hint: View) : AppCompatSpinner(context, mode) {

    private var wrapper: AdapterWrapper? = null

    override fun setAdapter(adapter: SpinnerAdapter?) {
        if (adapter == null) super.setAdapter(adapter)
        else {
            wrapper = AdapterWrapper(adapter)
            super.setAdapter(wrapper)
        }
    }

    override fun getAdapter(): SpinnerAdapter? {
        return super.getAdapter()
    }

    override fun setOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener?) {
        if (listener == null) {
            super.setOnItemClickListener(null)
            return
        }
        super.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                listener.onNothingSelected(parent)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val newPosition = if (position == count) -1 else position
                listener.onItemSelected(parent, view, newPosition, id)
            }
        })
    }

    override fun setSelection(position: Int) {
        if (position == -1) super.setSelection(count)
        else super.setSelection(position)
    }

    override fun getSelectedItemPosition(): Int {
        val superResult = super.getSelectedItemPosition()
        return if (superResult == count) -1
        else superResult
    }

    /*private var selectedItemPositionTopFlag = false
    override fun getSelectedItemPosition(): Int {
        // this toggle is required because this method will get called in other
        // places too, the most important being called for the
        // OnItemSelectedListener
        if (!selectedItemPositionTopFlag) {
            return 0; // get us to the first element
        }
        return super.getSelectedItemPosition();
    }
    override fun performClick(): Boolean {
        selectedItemPositionTopFlag = getSelectedItemPosition() == getCount()
        val result = super.performClick()
        selectedItemPositionTopFlag = false
        return result
    }*/

    private inner class AdapterWrapper(val innerAdapter: SpinnerAdapter) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            return if (position == count) {
                hint
            } else {
                val oldView = if (convertView != hint) convertView else null
                innerAdapter.getView(position, oldView, parent)
            }
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            return getView(position, convertView, parent!!)
        }

        override fun getItem(position: Int): Any? {
            return if (position < count) innerAdapter.getItem(position)
            else null
        }

        override fun getItemId(position: Int): Long {
            return if (position < count) innerAdapter.getItemId(position)
            else -1
        }

        override fun getCount(): Int {
            return innerAdapter.count
        }
    }
}