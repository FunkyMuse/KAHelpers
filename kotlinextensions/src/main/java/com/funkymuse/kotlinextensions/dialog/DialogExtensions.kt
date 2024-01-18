package com.funkymuse.kotlinextensions.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.funkymuse.setofusefulkotlinextensions.kotlinextensions.R


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


/**
 * Make the background transparent of the dialog window
 *
 * Should be called in the `onCreateView(inflater, container, savedInstanceState)` method
 * @receiver DialogFragment
 */
fun DialogFragment.transparentBackground() {
    dialog?.window?.let {
        it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        it.requestFeature(Window.FEATURE_NO_TITLE)
    }
}

/**
 * Should be called in the `onCreateView(inflater, container, savedInstanceState)` method
 *
 * @receiver DialogFragment
 */

fun DialogFragment.setRoundedBackground(setNoTitle: Boolean = false) {
    dialog?.window?.let {
        it.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.round_bg_theme_compatible))
        if (setNoTitle)
            it.requestFeature(Window.FEATURE_NO_TITLE)
    }
}

