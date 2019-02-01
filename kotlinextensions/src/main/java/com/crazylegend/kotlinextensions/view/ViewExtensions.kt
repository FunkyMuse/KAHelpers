package com.crazylegend.kotlinextensions.view

import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import org.joda.time.DateTime
import org.joda.time.LocalDate


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

fun DateTime.isToday(): Boolean {
    return this.toLocalDate() == LocalDate()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun TextInputEditText.getTheText(): String {
    return this.text.toString()
}

fun TextInputEditText.getTheTextTrimmed(): String {
    return this.text.toString().trim()
}

fun TextInputEditText.setTheText(text: String) {
    this.setText(text, android.widget.TextView.BufferType.EDITABLE)
}

fun EditText.getTheText(): String {
    return this.text.toString()
}

fun EditText.getTheTextTrimmed(): String {
    return this.text.toString().trim()
}

fun EditText.setTheText(text: String) {
    this.setText(text, android.widget.TextView.BufferType.EDITABLE)
}