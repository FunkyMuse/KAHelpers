package com.crazylegend.kotlinextensions.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
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

fun Dialog.setCustomTitle(resId: Int, title: String) {
    val titleView: TextView = findViewById(resId)
    titleView.text = title
}

fun Dialog.setCustomText(resId: Int, message: String) {
    val messageView: TextView = findViewById(resId)
    messageView.text = message
}

fun Dialog.setConfirmButtonText(resId: Int, text: String) {
    val confirmButton: TextView = findViewById(resId)
    confirmButton.text = text
}

fun Dialog.setCancelButtonText(resId: Int, text: String) {
    val cancelButton: TextView = findViewById(resId)
    cancelButton.text = text
}

fun Dialog.setConfirmButtonOnClick(resId: Int, clickListener: View.OnClickListener) {
    val confirmButton: TextView = findViewById(resId)
    confirmButton.setOnClickListener(clickListener)
}

fun Dialog.setCancelButtonOnClick(resId: Int, clickListener: View.OnClickListener) {
    val cancelButton: TextView = findViewById(resId)
    cancelButton.setOnClickListener(clickListener)
}

/**
 * Shows AlertDialog
 *
 * @param[themeId] Theme Id for Dialog
 * @param[cancelable] Whether dialog is cancelable or not, by default dialog is not cancelable,
 * use `true` to make it cancelable
 * @param[cancelableTouchOutside] Whether dialog is cancelled on touched outside or not, by
 * default it's not cancelable. Use `true` to make it cancelable when user touches outside of dialog
 * @param[builderFunction] Pass your `AlertDialog.Builder` functions which helps you to create
 * dialogs
 */
@JvmOverloads
inline fun Context.showDialog(themeId: Int = 0, cancelable: Boolean = false, cancelableTouchOutside: Boolean = false, builderFunction: AlertDialog.Builder.() -> Any) {
    val builder = AlertDialog.Builder(this, themeId)
    builder.builderFunction()
    val dialog = builder.create()

    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(cancelableTouchOutside)
    dialog.show()
}

/**
 * Helper method to create positive button for Alert dialog and listen to the button's click
 * @param[text] Positive Button Text, Defaults to "OK"
 * @param[handleClick] Callback that works as click listener when positive button is clicked
 *
 */
@JvmOverloads
inline fun AlertDialog.Builder.positiveButton(text: String = "OK", crossinline handleClick:
    (i: Int) -> Unit = {}) {
    this.setPositiveButton(text) { _, i -> handleClick(i) }
}

/**
 * Helper method to create negative button for Alert dialog and listen to the button's click
 * @param[text] Positive Button Text, Defaults to "CANCEL"
 * @param[handleClick] Callback that works as click listener when negative button is clicked
 *
 */
@JvmOverloads
inline fun AlertDialog.Builder.negativeButton(text: String = "CANCEL", crossinline handleClick: (i: Int) -> Unit = {}) {
    this.setNegativeButton(text) { _, i -> handleClick(i) }
}

/**
 * Helper method to create neutral button for Alert dialog and listen to the button's click
 * @param[text] Positive Button Text
 * @param[handleClick] Callback that works as click listener when neutral button is clicked
 *
 */
@JvmOverloads
inline fun AlertDialog.Builder.neutralButton(text: String, crossinline handleClick: (i: Int) -> Unit = {}) {
    this.setNeutralButton(text) { _, i -> handleClick(i) }
}