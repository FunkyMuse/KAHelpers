package com.crazylegend.kotlinextensions.customKeyboardView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.IdRes
import com.crazylegend.kotlinextensions.R


/**
 * Created by crazy on 10/24/18 to long live and prosper !
 */
class CustomKeyboardView : FrameLayout, View.OnClickListener {

    interface OnKeyListener {
        fun key(key: String)
    }

    companion object {
        var onKeyListener: OnKeyListener? = null
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.keyboard, this)
        initViews()
    }

    private fun initViews() {
        viewT<View>(R.id.t9_key_0).setOnClickListener(this)
        viewT<View>(R.id.t9_key_1).setOnClickListener(this)
        viewT<View>(R.id.t9_key_2).setOnClickListener(this)
        viewT<View>(R.id.t9_key_3).setOnClickListener(this)
        viewT<View>(R.id.t9_key_4).setOnClickListener(this)
        viewT<View>(R.id.t9_key_5).setOnClickListener(this)
        viewT<View>(R.id.t9_key_6).setOnClickListener(this)
        viewT<View>(R.id.t9_key_7).setOnClickListener(this)
        viewT<View>(R.id.t9_key_8).setOnClickListener(this)
        viewT<View>(R.id.t9_key_9).setOnClickListener(this)
        viewT<View>(R.id.t9_key_clear).setOnClickListener(this)
        viewT<View>(R.id.t9_key_backspace).setOnClickListener(this)


    }


    override fun onClick(v: View?) {
        if (v?.tag != null && "number_button" == v.tag) {
            onKeyListener?.key(((v as TextView).text).toString())
            return
        }
        when (v?.id) {
            R.id.t9_key_clear -> { // handle clear button
                onKeyListener?.key("clear")
            }
            R.id.t9_key_backspace -> { // handle backspace button
                // delete one character
                onKeyListener?.key("backspace")

            }
        }
    }

    private fun <T : View> viewT(@IdRes id: Int): T {
        return super.findViewById(id)
    }
}