package com.crazylegend.kotlinextensions.file

import android.content.ContentResolver
import android.content.ContentUris
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
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.FileProvider
import com.crazylegend.kotlinextensions.interfaces.OneParamInvocation
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by crazy on 2/24/19 to long live and prosper !
 */


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

/**
 * Read File data as String and Returns the Result
 */
fun File.readToString(): String {
    var text = ""
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
fun File.toByteArray(): ByteArray { val bos = ByteArrayOutputStream(this.length().toInt())
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
    } catch (@NonNull e: Exception) {
    }

}

fun deleteDir(@Nullable dir: File?): Boolean {
    if (dir != null && dir.isDirectory) {
        val children = dir.list()
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
    }
    return dir!!.delete()
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
    if ( DocumentsContract.isDocumentUri(context, this)) {
        // ExternalStorageProvider
        if (isExternalStorageDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex())
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

        } else if (isDownloadsDocument()) {

            val id = DocumentsContract.getDocumentId(this)
            val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

            return context.getDataColumn(contentUri, null, null)
        } else if (isMediaDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":".toRegex())
            val type = split[0]

            val contentUri: Uri?
            contentUri = when (type) {
                "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                else -> return null
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return context.getDataColumn(contentUri, selection, selectionArgs)
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
            val column_index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(column_index)
        }
    } finally {
        cursor?.close()
    }
    return null
}


fun downloadFile(urlPath :String, localPath :String, callback :(Uri?) -> Unit = {}) :Uri? {
    var uri :Uri? = null
    val connection = URL(urlPath).openConnection() as HttpURLConnection

    if(connection.responseCode == HttpURLConnection.HTTP_OK) {
        uri = Uri.fromFile(connection.inputStream.outAsFile(localPath.toFile()))
    }
    connection.disconnect()
    if(uri is Uri) {
        callback(uri)
    } else {
        callback(null)
    }
    return uri
}

fun downloadFile(urlPath :String, localPath :String, callback : OneParamInvocation<Uri>?) :Uri? {
    var uri :Uri? = null
    val connection = URL(urlPath).openConnection() as HttpURLConnection

    if(connection.responseCode == HttpURLConnection.HTTP_OK) {
        uri = Uri.fromFile(connection.inputStream.outAsFile(localPath.toFile()))
    }
    connection.disconnect()
    callback?.invoke(uri!!)
    return uri
}

fun String.toFile() = File(this)

fun saveFile(fullPath :String, content :String) :File =
        fullPath.toFile().apply {
            writeText(content, Charset.defaultCharset())
        }

fun File.readFile() :String = this.readText(Charset.defaultCharset())

private fun getDataColumnPrivate(context :Context, uri :Uri?, selection :String?, selectionArgs :Array<String>?) :String {
    uri?.let {
        context.contentResolver.query(it, arrayOf("_data"), selection, selectionArgs, null).use {
        if(it != null && it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow("_data")
            return it.getString(columnIndex)
        }
    }
    }
    return ""
}



 fun Uri.checkAuthority(context :Context) :String? {
    val docId = DocumentsContract.getDocumentId(this)
    val split = docId.split(":".toRegex()).dropLastWhile {it.isEmpty()}.toTypedArray()

    if("com.android.externalstorage.documents" == this.authority) {
        val type = split[0]

        if("primary".equals(type, ignoreCase = true))
            return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
    } else if("com.android.providers.downloads.documents" == this.authority) {
        return getDataColumnPrivate(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), docId.toLong()), null, null)
    } else if("com.android.providers.media.documents" == this.authority) {
        val contentUri = when(split[0]) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        return getDataColumnPrivate(context, contentUri, "_id=?", arrayOf(split[1]))
    }

    return this.path
}


 fun Context.getDataColumns(uri :Uri?, selection :String?, selectionArgs :Array<String>?) :String? {
    var cursor :Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = uri?.let { contentResolver.query(it, projection, selection, selectionArgs, null) }
        if(cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

 fun isExternalStorageDocument(uri :Uri) :Boolean = "com.android.externalstorage.documents" == uri.authority
 fun isDownloadsDocument(uri :Uri) :Boolean = "com.android.providers.downloads.documents" == uri.authority
 fun isMediaDocument(uri :Uri) :Boolean = "com.android.providers.media.documents" == uri.authority
 fun isGooglePhotosUri(uri :Uri) :Boolean = "com.google.android.apps.photos.content" == uri.authority

fun InputStream.getString(): String = this.bufferedReader().use {
    it.readText()
}

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
    val directory = File(Environment.getExternalStorageDirectory(), this)
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

fun File.lastModifiedDateTimeAsString(): String = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(lastModified())

val String.fileExtension
    get() = File(this).extension

fun File.mkdirsIfNotExist() = parentFile.exists() || parentFile.mkdirs()

/**
 * The method is use to prevent a problem, when several threads can try to [.mkdirsIfNotExist] for the same Path simultaneously.
 */
@Synchronized
fun File.mkdirsIfNotExistSynchronized() = mkdirsIfNotExist()

fun File.saveFile(runBlockIfOk: () -> Unit, runBlockIfFail: () -> Unit, useMkdirsIfNotExistSynchronized: Boolean) {
    if (if (useMkdirsIfNotExistSynchronized) mkdirsIfNotExistSynchronized() else mkdirsIfNotExist()) {
        runBlockIfOk()
    } else {
        runBlockIfFail()
    }
}


/**
 * Deletes given directory and returns result
 */
fun File.deleteDirectory(): Boolean {
    if (!exists()) return false
    return cleanDirectory()
}

/**
 * Force deletes given directory or file
 */
private fun File.forceDelete() {
    if (isDirectory) {
        deleteDirectory()
    } else {
        val filePresent = exists()
        if (!delete()) {
            if (!filePresent) {
                throw FileNotFoundException("File does not exist: $this")
            }
            val message = "Unable to delete file: $this"
            throw IOException(message)
        }
    }
}

/**
 * Cleans directory and returns result
 */
private fun File.cleanDirectory(): Boolean {
    val files = verifiedDirectoryFiles()
    var allFilesDeleted = true
    for (file in files) {
        try {
            forceDelete()
        } catch (e: IOException) {
            e.printStackTrace()
            if (allFilesDeleted) allFilesDeleted = false
        }
    }
    return allFilesDeleted
}

/**
 * Verifies files of directory
 */
private fun File.verifiedDirectoryFiles(): Array<File> {
    if (!exists()) {
        val message = toString() + " does not exist"
        throw IllegalArgumentException(message)
    }

    if (!isDirectory) {
        val message = toString() + " is not a directory"
        throw IllegalArgumentException(message)
    }

    return listFiles()
        ?: // null if security restricted
        throw IOException("Failed to list contents of $this")
}

fun <T: Any> T.getJarDirectory(): String? = File(this::class.java.protectionDomain?.codeSource?.location?.toURI()?.path).path


fun File.extractName(): String {
    return this.extractNameWithExtension().substringBeforeLast(".")
}

fun File.extractNameWithExtension(): String {
    return this.absolutePath.substringAfterLast("/")
}

fun File.extractExtension(): String {
    return this.absolutePath.substringAfterLast(".")
}