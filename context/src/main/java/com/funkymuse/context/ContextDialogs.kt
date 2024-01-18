package com.funkymuse.context

import android.content.Context
import androidx.appcompat.app.AlertDialog

/**
 * There is No Such Thing name Hard Toast, Its just an AlertDialog which will the [msg] you passed until user cancels it.
 */
fun Context.showToastHard(msg: String) = AlertDialog.Builder(this).setMessage(msg).show()

/**
 * Want to Confirm the User Action? Just call showConfirmationDialog with required + optional params to do so.
 */
fun Context.showConfirmationDialog(msg: String, onResponse: (result: Boolean) -> Unit, positiveText: String = "Yes", negativeText: String = "No", cancelable: Boolean = false) =
        AlertDialog.Builder(this).setMessage(msg).setPositiveButton(positiveText) { _, _ -> onResponse(true) }.setNegativeButton(
                negativeText
        ) { _, _ -> onResponse(false) }.setCancelable(cancelable).show()

/**
 * Want your user to choose Single thing from a bunch? call showSinglePicker and provide your options to choose from
 */
fun Context.showSinglePicker(choices: Array<String>, onResponse: (index: Int) -> Unit, checkedItemIndex: Int = -1) =
        AlertDialog.Builder(this).setSingleChoiceItems(choices, checkedItemIndex) { dialog, which ->
            onResponse(which)
            dialog.dismiss()
        }.show()

/**
 * Want your user to choose Multiple things from a bunch? call showMultiPicker and provide your options to choose from
 */
fun Context.showMultiPicker(choices: Array<String>, onResponse: (index: Int, isChecked: Boolean) -> Unit, checkedItems: BooleanArray? = null) =
        AlertDialog.Builder(this).setMultiChoiceItems(choices, checkedItems) { _, which, isChecked ->
            onResponse(
                    which,
                    isChecked
            )
        }.setPositiveButton("Done", null).show()