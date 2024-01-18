package com.funkymuse.kotlinextensions.views

import android.widget.CompoundButton

/**
 * Sets CompoundButton.OnCheckedChangeListener and calls specified function [block]
 * if this button state changed to checked
 *
 * @see CompoundButton.setOnCheckedChangeListener
 * @see CompoundButton.OnCheckedChangeListener
 */
inline fun CompoundButton.onChecked(crossinline block: () -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) block.invoke()
    }
}

/**
 * Sets CompoundButton.OnCheckedChangeListener and calls specified function [block]
 * if this button state changed to unchecked
 *
 * @see CompoundButton.setOnCheckedChangeListener
 * @see CompoundButton.OnCheckedChangeListener
 */
inline fun CompoundButton.onUnChecked(crossinline block: () -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        if (!isChecked) block.invoke()
    }
}

/**
 * Sets CompoundButton.OnCheckedChangeListener and calls specified function [block]
 * after checked state changed
 *
 * @return true - if checked, otherwise - false
 *
 * @see CompoundButton.setOnCheckedChangeListener
 * @see CompoundButton.OnCheckedChangeListener
 */
inline fun CompoundButton.onCheckedChange(crossinline block: (isChecked: Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        block.invoke(isChecked)
    }
}

