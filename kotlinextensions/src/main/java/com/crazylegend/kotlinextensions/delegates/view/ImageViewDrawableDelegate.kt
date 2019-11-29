package com.crazylegend.kotlinextensions.delegates.view

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isGone
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * Created by crazy on 11/29/19 to long live and prosper !
 */
class ImageViewDrawableDelegate(
        private val imageView: AppCompatImageView,
        private val hideWhenEmpty: Boolean = true
) : ReadWriteProperty<View, Drawable?> {

    private var value: Drawable? = null

    override fun getValue(thisRef: View, property: KProperty<*>): Drawable? {
        return value
    }

    override fun setValue(thisRef: View, property: KProperty<*>, value: Drawable?) {
        this.value = value
        imageView.setImageDrawable(value)
        imageView.isGone = hideWhenEmpty && value == null
    }
}