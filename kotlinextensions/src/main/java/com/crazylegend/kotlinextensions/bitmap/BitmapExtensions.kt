package com.crazylegend.kotlinextensions.bitmap

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */


data class FileAndUri(var file: File?, var path: String?)


@RequiresPermission(allOf = [WRITE_EXTERNAL_STORAGE])
fun Bitmap.createFileFromBitmap(
    mediaDir: String, imageExtension: String,
    compressionFormat: Bitmap.CompressFormat, compressionQuality: Int
): FileAndUri {

    val fileToReturn: File?


    val file: File?
    val uuid = UUID.randomUUID().toString()
    val directory = File(Environment.getExternalStorageDirectory(), mediaDir)


    file = if (directory.exists()) {
        File(directory, uuid.plus(imageExtension))

    } else {
        directory.mkdirs()

        File(directory, uuid.plus(imageExtension))

    }


    /*Single.fromCallable {
        Bitmap()
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).doOnSuccess {


    }*/


    val fos = FileOutputStream(file)
    this@createFileFromBitmap.compress(compressionFormat, compressionQuality, fos)
    fos.flush()
    fos.close()

    fileToReturn = file


    return FileAndUri(fileToReturn, fileToReturn.name)

}

@Throws(FileNotFoundException::class, SecurityException::class)
fun Uri.getBitmap(contentResolver: ContentResolver): Bitmap? {
    return MediaStore.Images.Media.getBitmap(contentResolver, this)
}


@RequiresApi(26)
fun Bitmap.toIcon(): Icon = Icon.createWithBitmap(this)

fun Bitmap.toDrawable(resources: Resources) = BitmapDrawable(resources, this)


fun Bitmap.toByteArray(compressFormat: Bitmap.CompressFormat, quality: Int): Single<ByteArray>? {

    val bos = ByteArrayOutputStream()

    return Single.fromCallable {
        this.compress(compressFormat, quality, bos)
        bos.toByteArray()
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).doAfterSuccess {
        bos.flush()
        bos.close()
    }

}

fun Activity.createColoredBitmapFullScreen(color: Int): Bitmap {


    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint()
    paint.color = color
    paint.style = Paint.Style.FILL
    canvas.drawPaint(paint)


    return bitmap
}


fun Activity.createColoredBitmap(color: Int, width: Int, height: Int): Bitmap {

    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint()
    paint.color = color
    paint.style = Paint.Style.FILL
    canvas.drawPaint(paint)


    return bitmap
}


fun ByteArray.getBitmap(): Single<Bitmap>? {

    return Single.fromCallable {
        BitmapFactory.decodeByteArray(this, 0, this.size)
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

}


@Throws(FileNotFoundException::class, IllegalArgumentException::class)
fun Context.getUriForFile(filePath: String, authority: String): Uri? {
    return FileProvider.getUriForFile(this, authority, File(filePath))
}

fun ContentResolver.getBitmap(imageUri: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(this, imageUri)
}


/**
 * Resize Bitmap to specified height and width.
 */
fun Bitmap.resize(newWidth: Number, newHeight: Number): Bitmap {
    val width = width
    val height = height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    if (width > 0 && height > 0) {
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
    return this
}


/**
 * Mthod to save Bitmap to specified file path.
 */
fun Bitmap.saveFile(path: String) {
    val f = File(path)
    if (!f.exists()) {
        f.createNewFile()
    }
    val stream = FileOutputStream(f)
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    stream.flush()
    stream.close()
}

/**
 * Returns Bitmap Width And Height Presented as a Pair of two Int where pair.first is width and pair.second is height
 */
val Bitmap.size: Pair<Int, Int>
    get() {
        return Pair(width, height)
    }

/**
 * get Pixels from Bitmap Easily
 */
operator fun Bitmap.get(x: Int, y: Int) = getPixel(x, y)


/**
 * set Pixels to Bitmap Easily
 */
operator fun Bitmap.set(x: Int, y: Int, pixel: Int) = setPixel(x, y, pixel)


/**
 * Crop image easily.
 * @param r is the Rect to crop from the Bitmap
 *
 * @return cropped #android.graphics.Bitmap
 */
fun Bitmap.crop(r: Rect) =
    if (Rect(0, 0, width, height).contains(r)) Bitmap.createBitmap(this, r.left, r.top, r.width(), r.height()) else null


/**
 * rotate a Bitmap with a ease
 */
fun Bitmap.rotateTo(angle: Float, recycle: Boolean = true): Bitmap {
    val matrix = Matrix()
    matrix.setRotate(angle)
    val newBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    if (recycle && isRecycled.not() && newBitmap != this)
        recycle()
    return newBitmap
}


/**
 * Greyscale the Image.
 */
fun Bitmap.toGrayScale(recycle: Boolean): Bitmap? {
    val ret = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(ret)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)
    val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = colorMatrixColorFilter
    canvas.drawBitmap(this, 0f, 0f, paint)
    if (recycle && !isRecycled && ret != this) recycle()
    return ret
}


/**
 * Blend the Bitmap Corners to Round with Given radius
 */
fun Bitmap.toRoundCorner(radius: Float, borderSize: Float = 0f, @ColorInt borderColor: Int = 0, recycle: Boolean = true): Bitmap {
    val width = width
    val height = height
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val ret = Bitmap.createBitmap(width, height, config)
    val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    paint.shader = shader
    val canvas = Canvas(ret)
    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
    val halfBorderSize = borderSize / 2f
    rectF.inset(halfBorderSize, halfBorderSize)
    canvas.drawRoundRect(rectF, radius, radius, paint)
    if (borderSize > 0) {
        paint.shader = null
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawRoundRect(rectF, radius, radius, paint)
    }
    if (recycle && !isRecycled && ret != this) recycle()
    return ret
}


/**
 * Makes the Bitmap Round with given params
 */
fun Bitmap.toRound(borderSize: Float = 0f, borderColor: Int = 0, recycle: Boolean = true): Bitmap {
    val width = width
    val height = height
    val size = Math.min(width, height)
    val paint = Paint(ANTI_ALIAS_FLAG)
    val ret = Bitmap.createBitmap(width, height, config)
    val center = size / 2f
    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
    rectF.inset((width - size) / 2f, (height - size) / 2f)
    val matrix = Matrix()
    matrix.setTranslate(rectF.left, rectF.top)
    if (width != height) {
        matrix.preScale(size.toFloat() / width, size.toFloat() / height)
    }
    val shader = BitmapShader(this, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    shader.setLocalMatrix(matrix)
    paint.shader = shader
    val canvas = Canvas(ret)
    canvas.drawRoundRect(rectF, center, center, paint)
    if (borderSize > 0) {
        paint.shader = null
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderSize
        val radius = center - borderSize / 2f
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)
    }
    if (recycle && !isRecycled && ret != this) recycle()
    return ret
}