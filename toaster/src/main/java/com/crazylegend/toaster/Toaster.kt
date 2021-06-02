package com.crazylegend.toaster

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Created by FunkyMuse on 1/13/20 to long live and prosper !
 */
class Toaster(private val context: Context) : ToasterContract {

    private var currentToast: Toast? = null

    override fun shortToast(@StringRes string: Int) {
        disposeCurrentToast()
        makeToastAndShow(string, Toast.LENGTH_SHORT)
    }

    override fun shortToast(string: String) {
        disposeCurrentToast()
        makeToastAndShow(string, Toast.LENGTH_SHORT)
    }

    override fun longToast(@StringRes string: Int) {
        disposeCurrentToast()
        makeToastAndShow(string, Toast.LENGTH_LONG)
    }

    override fun longToast(string: String) {
        disposeCurrentToast()
        makeToastAndShow(string, Toast.LENGTH_LONG)
    }

    private fun makeToastAndShow(string: Int, duration: Int) {
        currentToast = Toast.makeText(context, string, duration)
        currentToast?.show()
    }

    private fun makeToastAndShow(string: String, duration: Int) {
        currentToast = Toast.makeText(context, string, duration)
        currentToast?.show()
    }

    private fun disposeCurrentToast() {
        currentToast?.cancel()
        currentToast = null
    }
}