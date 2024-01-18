package com.funkymuse.kotlinextensions.bitmap

import android.graphics.Bitmap
import android.graphics.Matrix
import com.funkymuse.common.tryOrIgnore


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

