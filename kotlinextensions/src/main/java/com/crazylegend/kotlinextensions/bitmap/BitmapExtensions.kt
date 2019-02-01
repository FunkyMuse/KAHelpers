package com.crazylegend.kotlinextensions.bitmap

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
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
fun Bitmap.createFileFromBitmapPNG(mediaDir:String, imageExtension:String,
                                   compressionFormat:Bitmap.CompressFormat, compressionQuality : Int): FileAndUri {

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
    this@createFileFromBitmapPNG.compress(compressionFormat, compressionQuality, fos)
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


fun Bitmap.toByteArray(compressFormat: Bitmap.CompressFormat, quality:Int): Single<ByteArray>? {

    val bos = ByteArrayOutputStream()

    return Single.fromCallable{
        this.compress(compressFormat, quality, bos)
        bos.toByteArray()
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).doAfterSuccess {
        bos.flush()
        bos.close()
    }

}

fun Activity.createColoredBitmap(color:Int): Bitmap {


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



fun ByteArray.getBitmap(): Single<Bitmap>? {

    return Single.fromCallable {
        BitmapFactory.decodeByteArray(this, 0, this.size)
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())

}



@Throws(FileNotFoundException::class, IllegalArgumentException::class)
fun Context.getUriForFile(filePath: String, authority:String): Uri? {
    return FileProvider.getUriForFile(this, authority, File(filePath))
}

fun ContentResolver.getBitmap(imageUri: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(this, imageUri)
}