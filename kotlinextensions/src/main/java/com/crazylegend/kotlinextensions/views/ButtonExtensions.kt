package com.crazylegend.kotlinextensions.views

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ProgressBar


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */


private var viewOriginalHeight: Int = 0

fun Button.enableButtonWithLoading(progressBar: ProgressBar) {
    progressBar.visibility = View.GONE
    if (this.tag != null) {
        this.text = this.tag.toString()
    }
    this.isEnabled = true
    this.alpha = 1f
}

fun Button.disableButtonWithLoading(progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
    this.tag = this.text
    this.text = ""
    this.isEnabled = false
    this.alpha = 0.7.toFloat()
}

fun Button.enableButton() {
    this.isEnabled = true
    this.alpha = 1f
}

fun Button.disableButton() {
    this.isEnabled = false
    this.alpha = 0.7.toFloat()
}

var Button.compoundDrawableStart: Drawable?
    get() = compoundDrawablesRelative[0]
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(value,
            compoundDrawablesRelative[1],
            compoundDrawablesRelative[2],
            compoundDrawablesRelative[3])

var Button.compoundDrawableTop: Drawable?
    get() = compoundDrawablesRelative[1]
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative[0],
            value,
            compoundDrawablesRelative[2],
            compoundDrawablesRelative[3])

var Button.compoundDrawableEnd: Drawable?
    get() = compoundDrawablesRelative[2]
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative[0],
            compoundDrawablesRelative[1],
            value,
            compoundDrawablesRelative[3])

var Button.compoundDrawableBottom: Drawable?
    get() = compoundDrawablesRelative[3]
    set(value) = setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative[0],
            compoundDrawablesRelative[1],
            compoundDrawablesRelative[2],
            value)