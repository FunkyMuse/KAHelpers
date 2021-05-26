package com.crazylegend.kotlinextensions.bitmap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import com.crazylegend.common.tryOrIgnore


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

fun createBitmapWithMatrix(src: Bitmap, matrix: Matrix): Bitmap {
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

