package com.funkymuse.file

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.funkymuse.common.tryOrIgnore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.FileChannel
import java.nio.charset.Charset


/**
 * Deletes all files
 */
fun File.deleteAll() {
    if (isFile && exists()) {
        delete()
        return
    }
    if (isDirectory) {
        val files = listFiles()
        if (files == null || files.isEmpty()) {
            delete()
            return
        }
        files.forEach { it.deleteAll() }
        delete()
    }
}

fun File.doesNotExist() = !exists()

/**
 * Read File data as String and Returns the Result
 */
fun File.readToString(): String {
    var text: String
    open().use { inpS ->
        inpS.bufferedReader().use {
            text = it.readText()
            it.close()
        }
        inpS.close()
    }
    return text
}

/**
 * Open File in InputStream
 */
fun File.open(): InputStream = FileInputStream(this)


/**
 * Move File/Dir to new Destination
 */
fun File.move(dest: File) {
    if (isFile)
        renameTo(dest)
    else
        moveDirectory(dest)
}

/**
 * Copy File/Dir to new Destination
 */
fun File.copy(dest: File) {
    if (isDirectory)
        copyDirectory(dest)
    else
        copyFile(dest)
}


/**
 * returns true if File is an Image
 */
fun File.isImage(): Boolean {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    return try {
        val bitmap = BitmapFactory.decodeFile(absolutePath, options)
        val result = options.outWidth != -1 && options.outHeight != -1
        bitmap.recycle()
        return result
    } catch (e: Exception) {
        false
    }
}

/**
 * Convert File to ByteArray
 */
fun File.toByteArray(): ByteArray {
    val bos = ByteArrayOutputStream(this.length().toInt())
    val input = FileInputStream(this)
    val size = 1024
    val buffer = ByteArray(size)
    var len = input.read(buffer, 0, size)
    while (len != -1) {
        bos.write(buffer, 0, len)
        len = input.read(buffer, 0, size)
    }
    input.close()
    bos.close()
    return bos.toByteArray()
}

/**
 * Copies data from input stream
 */
fun File.copyFromInputStream(inputStream: InputStream) =
        inputStream.use { input -> outputStream().use { output -> input.copyTo(output) } }

fun Context.deleteCache() {
    try {
        val dir = this.cacheDir
        if (dir != null && dir.isDirectory) {
            deleteDir(dir)
        }
    } catch (e: Exception) {
    }

}

fun deleteDir(dir: File?): Boolean {
    if (dir != null && dir.isDirectory) {
        val children = dir.list()
        if (children.isNullOrEmpty()) return false
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
    }
    return dir?.delete() == true
}


fun String.toUri(): Uri {
    return Uri.parse(this)
}

fun File.toUri(): Uri {
    return Uri.fromFile(this)
}


fun File.copyInputStreamToFile(inputStream: InputStream) {
    inputStream.use { input ->
        this.outputStream().use { fileOut ->
            input.copyTo(fileOut)
        }
    }
}

// Private Methods
private fun File.copyFile(dest: File) {
    var fi: FileInputStream? = null
    var fo: FileOutputStream? = null
    var ic: FileChannel? = null
    var oc: FileChannel? = null
    try {
        if (!dest.exists()) {
            dest.createNewFile()
        }
        fi = FileInputStream(this)
        fo = FileOutputStream(dest)
        ic = fi.channel
        oc = fo.channel
        ic.transferTo(0, ic.size(), oc)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        fi?.close()
        fo?.close()
        ic?.close()
        oc?.close()
    }
}


private fun File.copyDirectory(dest: File) {
    if (!dest.exists()) {
        dest.mkdirs()
    }
    val files = listFiles()
    files?.forEach {
        if (it.isFile) {
            it.copyFile(File("${dest.absolutePath}/${it.name}"))
        }
        if (it.isDirectory) {
            val dirSrc = File("$absolutePath/${it.name}")
            val dirDest = File("${dest.absolutePath}/${it.name}")
            dirSrc.copyDirectory(dirDest)
        }
    }
}


private fun File.moveDirectory(dest: File) {
    copyDirectory(dest)
    deleteAll()
}

/**
 * Attempts to find and return the size of the file at the given [uri].
 */
fun ContentResolver.fileSize(uri: Uri): Long? {
    return openFileDescriptor(uri, "r")?.statSize
}


/**
 * Gets the actual path of the [Uri].
 */
fun Uri.getRealPath(context: Context): String? {


    // DocumentProvider
    if (DocumentsContract.isDocumentUri(context, this)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex())
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getRootDirectory().toString() + "/" + split[1]
            }

        } else if (isDownloadsDocument()) {

            val id = DocumentsContract.getDocumentId(this)
            val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

            return context.getDataColumn(contentUri, null, null)
        } else if (isMediaDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex())

            val contentUri: Uri? = when (split[0]) {
                "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                else -> return null
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return contentUri?.let { context.getDataColumn(it, selection, selectionArgs) }
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(scheme, ignoreCase = true)) {
        return context.getDataColumn(this, null, null)
    } else if ("file".equals(scheme, ignoreCase = true)) {
        return path
    }

    return null
}

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 * @param uri           The Uri to query.
 * *
 * @param selection     (Optional) Filter used in the query.
 * *
 * @param selectionArgs (Optional) Selection arguments used in the query.
 * *
 * @return The value of the _data column, which is typically a file path.
 */
fun Context.getDataColumn(uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {

    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = this.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(columnIndex)
        }
    } finally {
        cursor?.close()
    }
    return null
}


fun downloadFile(urlPath: String, localPath: String, callback: (Uri?) -> Unit = {}): Uri? {
    var uri: Uri? = null
    val connection = URL(urlPath).openConnection() as HttpURLConnection

    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
        uri = Uri.fromFile(connection.inputStream.outAsFile(localPath.toFile()))
    }
    connection.disconnect()
    if (uri is Uri) {
        callback(uri)
    } else {
        callback(null)
    }
    return uri
}

fun downloadFileWithProgress(urlPath: String, localPath: String,
                             connectionCallBack: (responseCode: Int) -> Unit = {},
                             onError: (Exception) -> Unit = {}, progress: (Int) -> Unit = {}, callback: (Uri?) -> Unit = {}) {
    val uri = localPath.toFile().toUri()
    val connection = URL(urlPath).openConnection() as HttpURLConnection
    val input = connection.inputStream
    val output = FileOutputStream(uri.toFile())
    try {
        connection.connect()

        val responseCode = connection.responseCode
        connectionCallBack(responseCode)
        if (responseCode != HttpURLConnection.HTTP_OK) {
            return
        }

        val fileLength = connection.contentLength
        val data = ByteArray(4096)
        var total: Long = 0
        var count: Int
        while (input.read(data).also { count = it } != -1) {
            total += count.toLong()
            if (fileLength > 0)
                progress((total * 100 / fileLength).toInt())
            output.write(data, 0, count)
        }
    } catch (e: Exception) {
        onError(e)
        return
    } finally {
        tryOrIgnore {
            output.close()
            input?.close()
        }
        connection.disconnect()
    }
    callback(uri)
}


fun String.toFile() = File(this)

fun saveFile(fullPath: String, content: String): File =
        fullPath.toFile().apply {
            writeText(content, Charset.defaultCharset())
        }


fun isExternalStorageDocument(uri: Uri): Boolean = "com.android.externalstorage.documents" == uri.authority
fun isDownloadsDocument(uri: Uri): Boolean = "com.android.providers.downloads.documents" == uri.authority
fun isMediaDocument(uri: Uri): Boolean = "com.android.providers.media.documents" == uri.authority
fun isGooglePhotosUri(uri: Uri): Boolean = "com.google.android.apps.photos.content" == uri.authority

fun InputStream.getString(): String = this.bufferedReader().readText()

fun InputStream.outAsFile(file: File): File {
    file.createNewFile()

    use { input ->
        file.outputStream().use { fileOut ->
            input.copyTo(fileOut)
        }
    }
    return file
}

fun InputStream.outAsBitmap(): Bitmap? = use {
    BitmapFactory.decodeStream(it)
}

/**
 * Gets an uri of file
 */
fun File.getUriFromFile(context: Context, authority: String): Uri {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        FileProvider.getUriForFile(context, authority, this)
    } else {
        Uri.fromFile(this)
    }
}


/**
 * Gets an uri of file
 */
fun Context.getUriFromFile(file: File, authority: String): Uri {
    return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        FileProvider.getUriForFile(this, authority, file)
    } else {
        Uri.fromFile(file)
    }
}


/**
 * Checks and returns if there's a valid directory with given path
 */
fun String.getAsDirectory(): File? {
    val directory = File(Environment.getRootDirectory(), this)
    return if (directory.exists()) {
        directory
    } else {
        null
    }
}

/**
 * Gets all files in given directory
 */
fun File.getFiles(): List<File> {
    val inFiles = ArrayList<File>()
    val files = this.listFiles()
    if (files != null) {
        for (file in files) {
            if (file.isDirectory) {
                inFiles.addAll(file.getFiles())
            } else {
                inFiles.add(file)
            }
        }
    }
    return inFiles
}

/**
 * Gets the file count of given directory
 */
fun File.getFileCount() = getFiles().size

/**
 * Calculates the folder size
 */
fun File.getFolderSize(): Long {
    var size: Long = 0
    if (isDirectory) {
        val files = listFiles()
        if (files != null) {
            for (file in files) {
                size += if (file.isDirectory) {
                    file.getFolderSize()
                } else {
                    file.length()
                }
            }
        } else {
            size = 0
        }
    } else {
        size = length()
    }

    return size
}
