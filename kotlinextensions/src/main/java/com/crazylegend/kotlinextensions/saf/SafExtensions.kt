package com.crazylegend.kotlinextensions.saf

import android.Manifest.permission.ACCESS_MEDIA_LOCATION
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresPermission
import androidx.documentfile.provider.DocumentFile
import androidx.exifinterface.media.ExifInterface
import com.crazylegend.kotlinextensions.intent.INTENT_TYPE_DOCUMENT
import java.io.*


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


/**
 * Recursive listing SAF files
 * @param files Array<DocumentFile>?
 */
fun recursiveSAFFiles(files: Array<DocumentFile>?, fileCallback: (traversedFile: DocumentFile) -> Unit = {}) {
    files?.apply {
        for (file in files) {
            if (file.isDirectory) {
                recursiveSAFFiles(file.listFiles(), fileCallback)
            } else {
                fileCallback.invoke(file)
            }
        }
    }
}

/**
 * Recursive listing SAF files
 * @param files Array<DocumentFile>?
 * @param callbackArray ArrayList<DocumentFile> the array that you add the items to
 */
fun recursiveSAFFiles(files: Array<DocumentFile>?, callbackArray: ArrayList<DocumentFile>) {
    files?.apply {
        for (file in files) {
            if (file.isDirectory) {
                recursiveSAFFiles(file.listFiles(), callbackArray)
            } else {
                callbackArray.add(file)
            }
        }
    }
}

/**
 * Recursive listing SAF Files and returns the list containing all the items
 * @param files Array<DocumentFile>?
 * @return MutableList<DocumentFile>
 */
fun recursiveSAFFiles(files: Array<DocumentFile>?): MutableList<DocumentFile> {
    val callbackArray = mutableListOf<DocumentFile>()
    files?.apply {
        for (file in files) {
            if (file.isDirectory) {
                recursiveSAFFiles(file.listFiles())
            } else {
                callbackArray.add(file)
            }
        }
    }

    return callbackArray
}


fun Context.moveFileToUri(treeUri: Uri, file: File, progress: (Long) -> Unit = {}) {
    contentResolver.openOutputStream(treeUri)?.use { output ->
        output as FileOutputStream
        FileInputStream(file).use { input ->
            output.channel.truncate(0)
            copyStream(file.length(), input, output) {
                progress.invoke(it)
            }
        }
    }
}

fun getMimeType(filePath: String?): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(filePath)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

fun File.getMimeType(): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(path)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

fun File.getMimeType(fallback: String = INTENT_TYPE_DOCUMENT): String {
    return MimeTypeMap.getFileExtensionFromUrl(toString())
            ?.run { MimeTypeMap.getSingleton().getMimeTypeFromExtension(toLowerCase()) }
            ?: fallback // You might set it to */*
}

fun copyStream(size: Long, inputStream: InputStream, os: OutputStream, progress: (Long) -> Unit = {}) {
    val bufferSize = 4096
    try {
        val bytes = ByteArray(bufferSize)
        var count = 0
        var prog = 0
        while (count != -1) {
            count = inputStream.read(bytes)
            if (count != -1) {
                os.write(bytes, 0, count)
                prog += count
                progress(prog.toLong() * 100 / size)
            }
        }
        os.flush()
        inputStream.close()
        os.close()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

