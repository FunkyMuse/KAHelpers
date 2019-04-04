package com.crazylegend.kotlinextensions.codestyle

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.crazylegend.kotlinextensions.locale.LocaleHelper
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible


/**
 * Created by hristijan on 4/2/19 to long live and prosper !
 */
abstract class BaseAbstractActivity : AppCompatActivity() {


    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
    }

    protected abstract fun getLayoutResourceId(): Int

    fun RelativeLayout.showProgressBar(position: Int = RelativeLayout.CENTER_IN_PARENT) {
        progressBar = ProgressBar(this@BaseAbstractActivity, null, android.R.attr.progressBarStyleLarge)
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(position)
        this.addView(progressBar, params)
        progressBar?.visible()
    }

    fun showProgressDialog(
        isCancellable: Boolean = true,
        loadingText: String = "Loading ...",
        loadingTextColor: Int = Color.parseColor("#000000")
    ) {

        val linearLayoutPadding = 30
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.setPadding(linearLayoutPadding, linearLayoutPadding, linearLayoutPadding, linearLayoutPadding)
        linearLayout.gravity = Gravity.CENTER
        var linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayoutParams.gravity = Gravity.CENTER
        linearLayout.layoutParams = linearLayoutParams

        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, linearLayoutPadding, 0)
        progressBar.layoutParams = linearLayoutParams

        linearLayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayoutParams.gravity = Gravity.CENTER
        val tvText = TextView(this)
        tvText.text = loadingText
        tvText.setTextColor(loadingTextColor)
        tvText.textSize = 20f
        tvText.layoutParams = linearLayoutParams

        linearLayout.addView(progressBar)
        linearLayout.addView(tvText)

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(isCancellable)
        builder.setView(linearLayout)

        progressDialog = builder.create()
        progressDialog?.show()
        val window = progressDialog?.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(progressDialog?.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            progressDialog?.window?.attributes = layoutParams
        }
    }

    private var progressDialog: AlertDialog? = null

    fun dismissProgressDialog(){
        progressDialog?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        progressDialog?.dismiss()
    }

    fun hideProgressBar() {
        progressBar?.gone()
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.onAttach(it) })
        /*
 //application level
class MainApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"))
    }
}*/

//setting the locale
//LocaleHelper.setLocale(context, "en")
    }
}