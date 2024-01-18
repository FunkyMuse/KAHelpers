package com.funkymuse.photos

import android.content.Context
import android.graphics.BitmapFactory
import android.media.ExifInterface
import androidx.annotation.DrawableRes
import java.io.File
import java.io.IOException



private val options: BitmapFactory.Options by lazy {
    val opt = BitmapFactory.Options()
    opt.inJustDecodeBounds = true
    opt
}


fun String.getImageWidth(): Int {
    BitmapFactory.decodeFile(this, options)
    return options.outWidth
}


fun String.getImageHeight(): Int {
    BitmapFactory.decodeFile(this, options)
    return options.outHeight
}


fun String.getImageMimeType(): String {
    BitmapFactory.decodeFile(this, options)
    return options.outMimeType ?: ""
}


fun Context.getImageWidth(@DrawableRes resId: Int): Int {
    BitmapFactory.decodeResource(this.resources, resId, options)
    return options.outWidth
}


fun Context.getImageHeight(@DrawableRes resId: Int): Int {
    BitmapFactory.decodeResource(this.resources, resId, options)
    return options.outHeight
}


fun Context.getImageMimeType(@DrawableRes resId: Int): String {
    BitmapFactory.decodeResource(this.resources, resId, options)
    return options.outMimeType ?: ""
}


fun getPhotoOrientationDegree(filePath: String?): Int {
    var degree = 0
    var exif: ExifInterface? = null

    if (filePath == null)
        return degree

    try {
        exif = ExifInterface(filePath)
    } catch (e: IOException) {
        println("Error: " + e.message)
    }

    if (exif != null) {
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
        if (orientation != -1) {
            degree = when (orientation) {
                ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> 0
                ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_FLIP_VERTICAL -> 180
                ExifInterface.ORIENTATION_ROTATE_90, ExifInterface.ORIENTATION_TRANSPOSE -> 90
                ExifInterface.ORIENTATION_ROTATE_270, ExifInterface.ORIENTATION_TRANSVERSE -> 270
                else -> 0
            }
        }
    }
    return degree
}


fun doesImageExist(image: String): Boolean {
    return File(image).exists()
}

fun isChoosedImage(image: String): Boolean {
    return image.startsWith("content://com.android.providers.media.documents/document")
}