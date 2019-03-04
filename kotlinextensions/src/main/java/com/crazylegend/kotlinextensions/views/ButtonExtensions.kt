package com.crazylegend.kotlinextensions.views

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