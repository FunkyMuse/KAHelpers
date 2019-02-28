package com.crazylegend.kotlinextensions.basehelpers

import android.view.View
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


/**
 * Created by crazy on 12/18/18 to long live and prosper !
 */
class SnackBarNoConnection(view:View) {

    private var snackbar:Snackbar = Snackbar.make(view, "No internet connection !", Snackbar.LENGTH_INDEFINITE)

    fun show(){
        snackbar.show()
    }

    fun hide(){
        snackbar.dismiss()
    }

    val isShown : Boolean get() = snackbar.isShown

    fun setSnackbarBehavior(behavior:BaseTransientBottomBar.Behavior){
        snackbar.behavior = behavior
    }
}