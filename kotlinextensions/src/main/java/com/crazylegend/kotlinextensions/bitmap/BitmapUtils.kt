package com.crazylegend.kotlinextensions.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import androidx.annotation.NonNull
import com.crazylegend.kotlinextensions.context.getCompatDrawable
import com.crazylegend.kotlinextensions.tryOrIgnore


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


fun Bitmap.flipHorizontally(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(1.0f, -1.0f)
    return createBitmapWithMatrix(this, matrix)
}

fun Bitmap.flipVertically(): Bitmap {
    val matrix = Matrix()
    matrix.preScale(-1.0f, 1.0f)
    return createBitmapWithMatrix(this, matrix)
}

fun createBitmapWithMatrix(@NonNull src: Bitmap, @NonNull matrix: Matrix): Bitmap {
    val dst = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    if (src != dst) {
        src.recycle()
    }
    return dst
}

fun Bitmap?.freeBitmap() {
    if (this == null || isRecycled) {
        return
    }

    tryOrIgnore {
        recycle()
    }
}


fun Context.getBitmapFromResource(drawableRes: Int): Bitmap? {
    var bitmap: Bitmap? = null
    val drawable = getCompatDrawable(drawableRes)
    val canvas = Canvas()
    drawable?.apply {
        bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        draw(canvas)
    }
    return bitmap
}