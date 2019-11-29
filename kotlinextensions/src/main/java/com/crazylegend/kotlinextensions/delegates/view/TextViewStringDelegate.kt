package com.crazylegend.kotlinextensions.delegates.view

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.crazylegend.kotlinextensions.views.setPrecomputedTextOrHide
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * Created by crazy on 11/29/19 to long live and prosper !
 */

class TextViewStringDelegate(
        private val textView: AppCompatTextView
) : ReadWriteProperty<View, String?> {

    private var value: String? = null

    override fun getValue(thisRef: View, property: KProperty<*>): String? {
        return value
    }

    override fun setValue(thisRef: View, property: KProperty<*>, value: String?) {
        this.value = value
        textView.setPrecomputedTextOrHide(value)
    }
}
