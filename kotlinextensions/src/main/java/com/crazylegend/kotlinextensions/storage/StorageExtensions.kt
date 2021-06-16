package com.crazylegend.kotlinextensions.storage

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import com.crazylegend.common.device.DeviceUtils
import com.crazylegend.intent.INTENT_TYPE_DOCUMENT
import com.crazylegend.intent.INTENT_TYPE_IMGs
import com.crazylegend.intent.INTENT_TYPE_PDF
import com.crazylegend.intent.INTENT_TYPE_VIDEO
import com.crazylegend.kotlinextensions.cursor.getStringOrNull
import java.io.*


/**
 * Created by hristijan on 8/8/19 to long live and prosper !
 */

/**
 *
 * @receiver Context
 * @param uri Uri
 * @return Bitmap?
 * @throws IOException
 */
@Throws(IOException::class)
fun Context.getBitmapFromUri(uri: Uri): Bitmap? {
    val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, STORAGE_READ_MODE)
    val fileDescriptor = parcelFileDescriptor?.fileDescriptor
    val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    return image
}

/**
 *
 * @receiver Context
 * @param uri Uri
 * @return String
 * @throws IOException
 */
@Throws(IOException::class)
fun Context.readTextFromUri(uri: Uri): String {
    val stringBuilder = StringBuilder()
    contentResolver.openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
        }
    }
    return stringBuilder.toString()
}

/**
 *
 * @receiver Context
 * @param uri Uri
 * @param onLineRead Function1<[@kotlin.ParameterName] String, Unit>
 * @return String
 * @throws IOException
 */
@Throws(IOException::class)
fun Context.readTextFromUri(uri: Uri, onLineRead: (line: String) -> Unit = {}): String {
    val stringBuilder = StringBuilder()
    contentResolver.openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                onLineRead(line)
                line = reader.readLine()
            }
        }
    }
    return stringBuilder.toString()
}

/**
 * If you have the URI for a document and the document's Document.COLUMN_FLAGS contains SUPPORTS_DELETE
 * @receiver Context
 * @param uri Uri
 */
fun Context.deleteDocument(uri: Uri) {
    DocumentsContract.deleteDocument(contentResolver, uri)
}

/**
 * Checks if file is virtual, from Google drive etc... use [getInputStreamForVirtualFile] to read its contents
 * @receiver Context
 * @param uri Uri
 * @return Boolean
 */
@RequiresApi(Build.VERSION_CODES.N)
fun Context.isVirtualFile(uri: Uri): Boolean {
    if (!DocumentsContract.isDocumentUri(this, uri)) {
        return false
    }

    val cursor: Cursor? = contentResolver.query(
            uri,
            arrayOf(DocumentsContract.Document.COLUMN_FLAGS),
            null,
            null,
            null
    )

    val flags: Int = cursor?.use {
        if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            0
        }
    } ?: 0

    return flags and DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT != 0
}

/**
 * Reads the contents of virtual file, check if it's virtual file first use [isVirtualFile]
 * @receiver Context
 * @param uri Uri
 * @param mimeTypeFilter String
 * @return FileInputStream?
 * @throws IOException
 */
@Throws(IOException::class)
fun Context.getInputStreamForVirtualFile(uri: Uri, mimeTypeFilter: String): FileInputStream? {

    val openableMimeTypes: Array<String>? = contentResolver.getStreamTypes(uri, mimeTypeFilter)

    return if (openableMimeTypes?.isNotEmpty() == true) {
        contentResolver
                .openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)?.createInputStream()
    } else {
        throw FileNotFoundException()
    }
}

/**
 * Sets the uri for read/write permission flags
 * @receiver Intent
 */
fun Intent.setURIRWPermissions() {
    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
}

/**
 * Sets the uri for write permission flags
 * @receiver Intent
 */
fun Intent.setUriWritePermission() {
    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
}

/**
 * Sets the uri for read permission flags
 * @receiver Intent
 */
fun Intent.setUriReadPermission() {
    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
}


const val takeFlags: Int =
        (Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)


/**
 * Take flags
 * @receiver ContentResolver
 * @param uri Uri
 */
fun ContentResolver.addTakeFlags(uri: Uri) {
    takePersistableUriPermission(uri, takeFlags)
}

fun Activity.openDocument(requestCode: Int, text: String = "Open document with...", onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        setURIRWPermissions()
        type = INTENT_TYPE_DOCUMENT
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(Intent.createChooser(intent, text), requestCode)
    } else {
        onCantHandleAction()
    }
}

fun Activity.openImage(requestCode: Int, text: String = "Open image with...", onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        setURIRWPermissions()
        type = INTENT_TYPE_IMGs
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(Intent.createChooser(intent, text), requestCode)
    } else {
        onCantHandleAction()
    }
}

fun Activity.openVideo(requestCode: Int, text: String = "Open video with...", onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        setURIRWPermissions()
        type = INTENT_TYPE_VIDEO
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(Intent.createChooser(intent, text), requestCode)
    } else {
        onCantHandleAction()
    }
}

fun Activity.openDirectory(requestCode: Int, text: String = "Open directory with...", onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    intent.setURIRWPermissions()
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(Intent.createChooser(intent, text), requestCode)
    } else {
        onCantHandleAction()
    }
}


fun Activity.pickMultipleImages(requestCode: Int, text: String = "Pick images with...", onCantHandleAction: () -> Unit = {}) {
    val intent = Intent()
    intent.type = INTENT_TYPE_IMGs
    intent.setURIRWPermissions()
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    intent.action = Intent.ACTION_GET_CONTENT
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(Intent.createChooser(intent, text), requestCode)
    } else {
        onCantHandleAction()
    }
}


fun Activity.pickImage(PICK_PHOTO_REQUEST_CODE: Int, text: String = "Pick image with...", onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    )
    intent.setURIRWPermissions()
    intent.type = INTENT_TYPE_IMGs
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(Intent.createChooser(intent, text), PICK_PHOTO_REQUEST_CODE)
    } else {
        onCantHandleAction()
    }
}


fun Activity.pickVideo(PICK_VIDEO_REQUEST_CODE: Int, text: String = "Open video with...", onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    )
    intent.setURIRWPermissions()
    intent.type = INTENT_TYPE_VIDEO
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(Intent.createChooser(intent, text), PICK_VIDEO_REQUEST_CODE)
    } else {
        onCantHandleAction()
    }
}


fun Activity.pickPDF(PICK_PDF_CODE: Int, text: String = "Pick pdf with...", onCantHandleAction: () -> Unit = {}) {
    val intentPDF = Intent(Intent.ACTION_GET_CONTENT)
    intent.setURIRWPermissions()
    intentPDF.type = INTENT_TYPE_PDF
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(Intent.createChooser(intent, text), PICK_PDF_CODE)
    } else {
        onCantHandleAction()
    }
}

/**
 * Returns the file size in bytes and the file name
 * @receiver Context
 * @param documentUri Uri
 * @param callback Function2<[@kotlin.ParameterName] String?, [@kotlin.ParameterName] String?, Unit>
 */
fun Context.getDocumentNameAndSizeForUri(documentUri: Uri, callback: (size: String?, name: String?) -> Unit = { _, _ -> }) {
    contentResolver.query(documentUri, null, null, null, null)?.use { cursor ->
        // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
        // "if there's anything to look at, look at it" conditionals.
        if (cursor.moveToFirst()) {
            // Note it's called "Display Name".  This is
            // provider-specific, and might not necessarily be the file name.
            cursor.getStringOrNull(OpenableColumns.DISPLAY_NAME)
            callback(cursor.getStringOrNull(OpenableColumns.SIZE), cursor.getStringOrNull(OpenableColumns.DISPLAY_NAME))
        } else {
            //nulls
            callback(null, null)
        }
    }
}

// Here are some examples of how you might call this method.
// The first parameter is the MIME type, and the second parameter is the name
// of the file you are creating:
//
// createFile("text/plain", "foobar.txt");
// createFile("image/png", "mypicture.png");
fun Activity.createFile(mimeType: String, fileNameAndExtension: String, WRITE_REQUEST_CODE: Int, onCantHandleAction: () -> Unit = {}) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        addCategory(Intent.CATEGORY_OPENABLE)

        // Create a file with the requested MIME type.
        type = mimeType
        putExtra(Intent.EXTRA_TITLE, fileNameAndExtension)
    }
    if (intent.resolveActivity(packageManager) != null) {
        startActivityForResult(intent, WRITE_REQUEST_CODE)
    } else {
        onCantHandleAction()
    }
}


/**
 * REturns the size if it's known in bytes otherwise "Unknown" and the name
 * @receiver Context
 * @param uri Uri
 * @param callback Function2<[@kotlin.ParameterName] String?, [@kotlin.ParameterName] String?, Unit>
 */
fun Context.dumpImageMetaData(uri: Uri, callback: (size: String?, name: String?) -> Unit) {

    // The query, since it only applies to a single document, will only return
    // one row. There's no need to filter, sort, or select fields, since we want
    // all fields for one document.
    val cursor: Cursor? = contentResolver.query(uri, null, null, null, null, null)

    cursor?.use {
        // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
        // "if there's anything to look at, look at it" conditionals.
        if (it.moveToFirst()) {

            // Note it's called "Display Name".  This is
            // provider-specific, and might not necessarily be the file name.
            val displayName: String =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))

            val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)
            // If the size is unknown, the value stored is null.  But since an
            // int can't be null in Java, the behavior is implementation-specific,
            // which is just a fancy term for "unpredictable".  So as
            // a rule, check if it's null before assigning to an int.  This will
            // happen often:  The storage API allows for remote files, whose
            // size might not be locally known.
            val size: String = if (!it.isNull(sizeIndex)) {
                // Technically the column stores an int, but cursor.getString()
                // will do the conversion automatically.
                it.getString(sizeIndex)
            } else {
                "Unknown"
            }
            callback(size, displayName)
        }
    }
}

/**
 * Called after receiving the uri in onActivityResult when called by [createFile]
 * @receiver Context
 * @param uri Uri
 * @param bytes ByteArray
 */
fun Context.alterDocument(uri: Uri, b: Int) {
    try {
        contentResolver.openFileDescriptor(uri, STORAGE_WRITE_MODE)?.use {
            // use{} lets the document provider know you're done by automatically closing the stream
            FileOutputStream(it.fileDescriptor).use {
                it.write(b)
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

const val STORAGE_READ_MODE = "r"
const val STORAGE_WRITE_MODE = "w"


/**
 * Called after receiving the uri in onActivityResult when called by [createFile]
 * @receiver Context
 * @param uri Uri
 * @param bytes ByteArray
 */
fun Context.alterDocument(uri: Uri, bytes: ByteArray) {
    try {
        val cr = contentResolver
        cr.takePersistableUriPermission(uri, takeFlags)
        cr.openFileDescriptor(uri, STORAGE_WRITE_MODE)?.use {
            // use{} lets the document provider know you're done by automatically closing the stream
            FileOutputStream(it.fileDescriptor).use {
                it.write(bytes, 0, bytes.size)
            }
        }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


/**
 * Checks if external storage is available for read and write
 */
val isExternalStorageWritable
    get() :Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

/**
 * Checks if external storage is available to at least read
 */
val isExternalStorageReadable
    get() : Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

/**
 * Checks if [Environment].MEDIA_MOUNTED is returned by `getExternalStorageState()`
 * and therefore external storage is read- and writable.
 */
val isExtStorageAvailable
    get() : Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
    }

val isDiskEncrypted = DeviceUtils.getSystemProperty("ro.crypto.state").equals("encrypted", true)

