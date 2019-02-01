package com.crazylegend.kotlinextensions.bitmap

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */


data class FileAndUri(var file: File?, var path: String?)


@RequiresPermission(allOf = [WRITE_EXTERNAL_STORAGE])
fun Bitmap.createFileFromBitmapPNG(context: Context, mediaDir:String, imageExtension:String,
                                   compressionFormat:Bitmap.CompressFormat, compressionQuality : Int): FileAndUri {

    var fileToReturn: File?


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


@Throws(FileNotFoundException::class, IllegalArgumentException::class)
fun Context.getUriForFile(filePath: String, authority:String): Uri? {
    return FileProvider.getUriForFile(this, authority, File(filePath))
}

fun ContentResolver.getBitmap(imageUri: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(this, imageUri)
}