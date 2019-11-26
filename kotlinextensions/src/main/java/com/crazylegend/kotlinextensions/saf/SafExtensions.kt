package com.crazylegend.kotlinextensions.saf

import android.Manifest.permission.ACCESS_MEDIA_LOCATION
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.documentfile.provider.DocumentFile
import androidx.exifinterface.media.ExifInterface
import java.io.File


/**
 * Created by crazy on 11/11/19 to long live and prosper !
 */

/**
 * Extracts user's location from images, requires permission
 * @see [ACCESS_MEDIA_LOCATION]
 * @receiver Context
 * @param uri Uri
 * @param latNLongCallBack Function1<[@kotlin.ParameterName] DoubleArray, Unit>
 */
@RequiresPermission(ACCESS_MEDIA_LOCATION)
fun Context.getLocationFromImages(uri: Uri, latNLongCallBack: (latNLong: DoubleArray) -> Unit = { _ -> }) {
    val photoUri = MediaStore.setRequireOriginal(uri)
    contentResolver.openInputStream(photoUri).use { stream ->
        stream?.let {
            ExifInterface(it).run {
                // If lat/long is null, fall back to the coordinates (0, 0).
                val latLong = this.latLong ?: doubleArrayOf(0.0, 0.0)
                latNLongCallBack(latLong)
            }
        }
    }
}

/**
 * Create a [DocumentFile] representing the document tree rooted at
 * the given {@link Uri}. This is only useful on devices running
 * {@link android.os.Build.VERSION_CODES#LOLLIPOP} or later, and will return
 * {@code null} when called on earlier platform versions.
 * @receiver Uri
 * @param context Context
 * @return DocumentFile?
 */
fun Uri.asDocumentFileTree(context: Context) = DocumentFile.fromTreeUri(context, this)


/**
 * Create a {@link DocumentFile} representing the single document at the
 * given {@link Uri}. This is only useful on devices running
 * {@link android.os.Build.VERSION_CODES#KITKAT} or later, and will return
 * {@code null} when called on earlier platform versions.
 * @receiver Uri
 * @param context Context
 * @return DocumentFile?
 */
fun Uri.asDocumentSingleUri(context: Context) = DocumentFile.fromSingleUri(context, this)


/**
 * Create a [DocumentFile] representing the filesystem tree rooted at
 * the given [File]. This doesn't give you any additional access to the
 * underlying files beyond what your app already has.
 * @receiver File
 * @return DocumentFile
 */
fun File.asDocumentFile() = DocumentFile.fromFile(this)