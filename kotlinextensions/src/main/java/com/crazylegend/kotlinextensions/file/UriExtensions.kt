package com.crazylegend.kotlinextensions.file

import android.content.ClipData
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.io.File


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */


/**
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun Uri.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents" == authority
}

/**
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun Uri.isDownloadsDocument(): Boolean {
    return "com.android.providers.downloads.documents" == authority
}

/**
 * @return Whether the Uri authority is MediaProvider.
 */
fun Uri.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents" == authority
}

/**
 * Opens and reads the entirety of the Uri into a ByteArray.
 */
fun Uri.toByteArray(context: Context): ByteArray? {
    return context.contentResolver.openInputStream(this)?.run {
        val array = this.readBytes()
        close()
        array
    }
}

fun Uri.resolveMimeType(context: Context): String? {
    val mimeType: String?
    mimeType = if (scheme == ContentResolver.SCHEME_CONTENT) {
        val cr = context.contentResolver
        cr.getType(this)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
    }
    return mimeType
}

/**
 * Tries to return the mime type from the file path
 */
fun Uri.resolveMimeTypeFromFilePart(): String? {
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(toString())
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
}

fun Uri.resolveDisplayName(context: Context): String? {
    val uriString = toString()
    if (uriString.startsWith("content://", true)) {
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(this, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex > -1) {
                    return cursor.getString(columnIndex)
                }
            }
        } finally {
            cursor?.close()
        }
    } else if (uriString.startsWith("file://", true)) {
        return File(uriString).name
    }
    return lastPathSegment
}

val Uri.isTelephoneLink: Boolean
    get() = toString().startsWith("tel:")

val Uri.isMailToLink: Boolean
    get() = toString().startsWith("mailto:")


fun Uri?.readBytes(context: Context): ByteArray? {
    return this?.let {
        return@let context.contentResolver.openInputStream(it)?.use {
            return@use it.readBytes()
        }
    }
}

fun ClipData?.multipleUriHandle(context: Context): ArrayList<ByteArray?> {
    val resultList: ArrayList<ByteArray?> = ArrayList()
    if (this != null) {
        //multiple images
        for (i in 0 until itemCount) {
            val imageUri = getItemAt(i)?.uri
            if (imageUri != null) {
                resultList.add(imageUri.readBytes(context))
            }
        }
    }

    return resultList
}
