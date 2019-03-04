package com.crazylegend.kotlinextensions.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable


/**
 * Created by hristijan on 3/4/19 to long live and prosper !
 */
/**
 * Converts a Bitmap to a Drawable
 */
fun Context.bitmapToDrawable(bitmap: Bitmap?): Drawable? = BitmapDrawable(this.resources, bitmap)

/**
 * Converts this Bitmap to a Drawable
 */
fun Bitmap.toDrawable(context: Context): Drawable? = BitmapDrawable(context.resources, this)

