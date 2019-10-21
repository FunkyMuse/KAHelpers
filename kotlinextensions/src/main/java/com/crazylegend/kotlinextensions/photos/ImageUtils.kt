package com.crazylegend.kotlinextensions.photos

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by hristijan on 4/8/19 to long live and prosper !
 */


fun ExifInterface.getRotationMatrix() =
        when (getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> Matrix().apply { postRotate(90f) }   //6
            ExifInterface.ORIENTATION_ROTATE_180 -> Matrix().apply { postRotate(180f) } //3
            ExifInterface.ORIENTATION_ROTATE_270 -> Matrix().apply { postRotate(270f) } //8

            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> Matrix().apply { postScale(-1f, 1f) } //2
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> Matrix().apply { postScale(1f, -1f) } //4

            ExifInterface.ORIENTATION_TRANSPOSE -> Matrix().apply { postRotate(90f); postScale(-1f, 1f) } //5
            ExifInterface.ORIENTATION_TRANSVERSE -> Matrix().apply { postRotate(270f); postScale(-1f, 1f) } //7

            else -> null
        }

@SuppressLint("SimpleDateFormat")
fun ExifInterface.getDateTimeAsDate() =
        try {
            getAttribute(ExifInterface.TAG_DATETIME)?.let { attr ->
                // JavaDoc from ExifInterface.TAG_DATETIME:
                // The format is "YYYY:MM:DD HH:MM:SS" with time shown in 24-hour format
                SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(attr)
            }
        } catch (e: Exception) {
            null
        }

fun Bitmap.applyMatrix(matrix: Matrix?): Bitmap =
        if (matrix == null) this
        else Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)

/**
 * decode image and apply correct rotation according to the exif orientation tag
 * use Context.decodeImageFileEx, to support content:// uris
 * @see Context.decodeImageFileEx
 *
 * @param path file path of the image
 * @param maxSize image will be decoded with an appropriate sample to ensure max(width, height) <= maxSize
 * @param canvasHandler can be used to add watermarks, etc.
 */
fun decodeImageFileEx(path: String, maxSize: Int = 0, canvasHandler: (Canvas.() -> Unit)? = null): Bitmap? =
        decodeImageFileInternal({
            BitmapFactory.decodeFile(path, it)
        }, {
            ExifInterface(path).getRotationMatrix()
        }, maxSize, canvasHandler)

/**
 * same as decodeImageFileEx, but uses FileDescriptors to access the files
 * @see decodeImageFileEx
 *
 * @param uri uri of the image file; content:// uris are supported
 * @param maxSize image will be decoded with an appropriate sample to ensure max(width, height) <= maxSize
 * @param canvasHandler can be used to add watermarks, etc.
 */
fun Context.decodeImageFileEx(uri: Uri, maxSize: Int = 0, canvasHandler: (Canvas.() -> Unit)? = null): Bitmap? =
        decodeImageFileInternal({ opts ->
            contentResolver.openFileDescriptor(uri, "r").use {
                it ?: return@use null
                BitmapFactory.decodeFileDescriptor(it.fileDescriptor, null, opts)
            }
        }, {
            contentResolver.openFileDescriptor(uri, "r").use {
                it ?: return@use null
                ExifInterface(it.fileDescriptor.toString()).getRotationMatrix()
            }
        }, maxSize, canvasHandler)

private fun decodeImageFileInternal(bfFun: (BitmapFactory.Options) -> Bitmap?,
                                    matrixFun: () -> Matrix?,
                                    maxSize: Int, canvasHandler: (Canvas.() -> Unit)?): Bitmap? {
    val opts = BitmapFactory.Options()
    if (maxSize > 0) {
        opts.inJustDecodeBounds = true
        bfFun(opts)
        val size = minOf(opts.outWidth, opts.outHeight)
        if (size <= 0) return null
        opts.inSampleSize = maxOf((size.toFloat() / maxSize.toFloat()).roundToInt(), 1)
        opts.inJustDecodeBounds = false
    }
    if (canvasHandler != null) {
        opts.inMutable = true
    }
    return bfFun(opts)?.applyMatrix(matrixFun())?.also {
        if (it.isMutable) canvasHandler?.invoke(Canvas(it))
    }
}


@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
private fun Activity.createImageFile(): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("dd_MM_yyyy_HHmmss").format(Date())
    val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
    )
}

@RequiresPermission(CAMERA)
fun Activity.dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE: Int = 313, photoFileCallback: (file: File) -> Unit = {}) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        // Ensure that there's a camera activity to handle the intent
        takePictureIntent.resolveActivity(packageManager)?.also {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                null
            }

            // Continue only if the File was successfully created
            photoFile?.also {
                photoFileCallback(it)
                val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        packageName,
                        it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
}
