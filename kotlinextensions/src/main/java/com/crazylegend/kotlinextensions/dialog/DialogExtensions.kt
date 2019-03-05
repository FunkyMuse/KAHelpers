package com.crazylegend.kotlinextensions.dialog

import android.app.Dialog
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.fragment.app.DialogFragment
import com.crazylegend.kotlinextensions.context.*


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */

fun Dialog.color(@ColorRes clr: Int): Int {
    return context.color(clr)
}

fun Dialog.string(@StringRes str: Int): String {
    return context.string(str)
}

fun Dialog.drawable(@DrawableRes drw: Int): Drawable? {
    return context.drawable(drw)
}

fun Dialog.dimen(@DimenRes dmn: Int): Float {
    return context.dimen(dmn)
}

fun Dialog.dimenInt(@DimenRes dmn: Int): Int {
    return context.dimenInt(dmn)
}

fun Dialog.int(@IntegerRes int: Int): Int {
    return context.int(int)
}

fun Dialog.font(@FontRes font: Int): Typeface? {
    return context.font(font)
}

fun Dialog.stringArray(array: Int): Array<String> {
    return context.stringArray(array)
}

fun Dialog.intArray(array: Int): IntArray {
    return context.intArray(array)
}

fun DialogFragment.color(@ColorRes clr: Int): Int? {
    return context?.color(clr)
}

fun DialogFragment.string(@StringRes str: Int): String? {
    return context?.string(str)
}

fun DialogFragment.drawable(@DrawableRes drw: Int): Drawable? {
    return context?.drawable(drw)
}

fun DialogFragment.dimen(@DimenRes dmn: Int): Float? {
    return context?.dimen(dmn)
}

fun DialogFragment.dimenInt(@DimenRes dmn: Int): Int?{
    return context?.dimenInt(dmn)
}

fun DialogFragment.int(@IntegerRes int: Int): Int? {
    return context?.int(int)
}

fun DialogFragment.font(@FontRes font: Int): Typeface? {
    return context?.font(font)
}

fun DialogFragment.stringArray(array: Int): Array<String>? {
    return context?.stringArray(array)
}

fun DialogFragment.intArray(array: Int): IntArray? {
    return context?.intArray(array)
}