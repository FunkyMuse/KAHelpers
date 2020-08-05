package com.crazylegend.kotlinextensions.file

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
import android.webkit.MimeTypeMap

import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.crazylegend.kotlinextensions.gson.fromJsonTypeToken
import com.crazylegend.kotlinextensions.interfaces.OneParamInvocation
import com.crazylegend.kotlinextensions.log.debug
import com.google.gson.Gson
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


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

fun downloadFile(urlPath: String, localPath: String, callback: OneParamInvocation<Uri>?): Uri? {
    var uri: Uri? = null
    val connection = URL(urlPath).openConnection() as HttpURLConnection

    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
        uri = Uri.fromFile(connection.inputStream.outAsFile(localPath.toFile()))
    }
    connection.disconnect()
    callback?.invoke(uri!!)
    return uri
}

fun String.toFile() = File(this)

fun saveFile(fullPath: String, content: String): File =
        fullPath.toFile().apply {
            writeText(content, Charset.defaultCharset())
        }


private fun getDataColumnPrivate(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String {
    uri?.let {
        context.contentResolver.query(it, arrayOf("_data"), selection, selectionArgs, null).use {
            if (it != null && it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow("_data")
                return it.getString(columnIndex)
            }
        }
    }
    return ""
}


fun Context.getDataColumns(uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)

    try {
        cursor = uri?.let { contentResolver.query(it, projection, selection, selectionArgs, null) }
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
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

fun File.lastModifiedDateTimeAsString(): String = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(lastModified())

val String.fileExtension
    get() = File(this).extension

fun File.mkdirsIfNotExist() = parentFile?.exists() ?: false || parentFile?.mkdirs() ?: false

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

fun <T : Any> T.getJarDirectory(): String? = File(this::class.java.protectionDomain?.codeSource?.location?.toURI()?.path.toString()).path


fun File.extractName(): String {
    return this.extractNameWithExtension().substringBeforeLast(".")
}

fun File.extractNameWithExtension(): String {
    return this.absolutePath.substringAfterLast("/")
}

fun File.extractExtension(): String {
    return this.absolutePath.substringAfterLast(".")
}

/**
 * Recursive listing [File] files
 * @param files Array<File>?
 * @param fileCallback Function1<[@kotlin.ParameterName] File, Unit>
 */
fun recursiveDirectoryListing(files: Array<File>?, fileCallback: (file: File) -> Unit = {}) {
    files?.apply {
        for (file in files) {
            if (file.isDirectory) {
                recursiveDirectoryListing(file.listFiles(), fileCallback)
            } else {
                fileCallback.invoke(file)
            }
        }
    }
}

/**
 * Recursively listing [File] files
 * @param files Array<File>?
 * @param callbackArray ArrayList<File>
 */
fun recursiveDirectoryListing(files: Array<File>?, callbackArray: ArrayList<File>) {
    files?.apply {
        for (file in files) {
            if (file.isDirectory) {
                recursiveDirectoryListing(file.listFiles(), callbackArray)
            } else {
                callbackArray.add(file)
            }
        }
    }
}

/**
 * Recursively listing [File] files returning the list of all files
 * @param files Array<File>?
 * @return MutableList<File>
 */
fun recursiveDirectoryListing(files: Array<File>?): MutableList<File> {
    val callbackArray = mutableListOf<File>()
    files?.apply {
        for (file in files) {
            if (file.isDirectory) {
                recursiveDirectoryListing(file.listFiles())
            } else {
                callbackArray.add(file)
            }
        }
    }

    return callbackArray
}

fun File.clearFile() {
    writeBytes(ByteArray(0))
}

fun File.clearFileAndWriteBytes(byteArray: ByteArray) {
    clearFile()
    appendBytes(byteArray)
}


fun File.clearFileAndWriteText(text: String, charset: Charset = Charsets.UTF_8) {
    clearFile()
    appendText(text, charset)
}


fun File.deleteSafely(): Boolean {
    return if (exists()) {
        delete()
    } else {
        false
    }
}


inline fun <reified T> File.asJsonFromText(charset: Charset = Charsets.UTF_8) {
    Gson().fromJsonTypeToken<T>(readText(charset))
}

@RequiresApi(Build.VERSION_CODES.N)
fun Context.clearDataDir() = dataDir.deleteSafely()

fun Context.clearCacheDir() = cacheDir.deleteSafely()

fun Context.clearObbDir() = obbDir.deleteSafely()

fun Context.clearFilesDir() = filesDir.deleteSafely()


/**
 * Get myme type of file
 *
 * @param url url of file
 * @return application/'fileType' like **application/pdf**
 */
fun getMimeType(url: String): String? {
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(url)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}


/**
 * Get the size of the directory in bytes format
 *
 */
fun File.getDirectorySize(): Long {
    if (exists()) {
        var result: Long = 0
        val fileList = listFiles()
        for (aFileList in fileList!!) {
            result += if (aFileList.isDirectory) {
                getDirectorySize()
            } else {
                aFileList.length()
            }
        }
        return result
    }
    return 0
}

/**
 * Create zip of files
 *
 * @param files no. of files to make a zip
 * @param file  where it is stored
 * @return file/folder path on successfull process
 */
fun createZip(files: List<String>, file: File, bufferSize: Int = 2048): String? {
    try {
        var origin: BufferedInputStream
        val dest = FileOutputStream(file)
        val out = ZipOutputStream(BufferedOutputStream(
                dest))

        val data = ByteArray(bufferSize)
        for (i in files.indices) {
            val fi = FileInputStream(files[i])
            origin = BufferedInputStream(fi, bufferSize)

            val entry = ZipEntry(files[i].substring(
                    files[i].lastIndexOf("/") + 1))
            out.putNextEntry(entry)
            val count: Int = origin.read(data, 0, bufferSize)

            while (count != -1) {
                out.write(data, 0, count)
            }
            origin.close()
        }

        out.close()
        return file.toString()
    } catch (ignored: Exception) {
        ignored.printStackTrace()
    }

    return null
}


/**
 * Zip the files.
 *
 * @param srcFiles    The source of files.
 * @param zipFilePath The path of ZIP file.
 * @return `true`: success<br></br>`false`: fail
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun zipFiles(srcFiles: Collection<String>,
             zipFilePath: String): Boolean {
    return zipFiles(
            srcFiles,
            zipFilePath,
            null
    )
}


/**
 * Zip the files.
 *
 * @param srcFilePaths The paths of source files.
 * @param zipFilePath  The path of ZIP file.
 * @param comment      The comment.
 * @return `true`: success<br></br>`false`: fail
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun zipFiles(srcFilePaths: Collection<String>?,
             zipFilePath: String?,
             comment: String?): Boolean {
    if (srcFilePaths == null || zipFilePath == null) return false
    var zos: ZipOutputStream? = null
    try {
        zos = ZipOutputStream(FileOutputStream(zipFilePath))
        for (srcFile in srcFilePaths) {
            if (!createZipFile(
                            getFileByPath(srcFile)!!,
                            "",
                            zos,
                            comment
                    )
            ) return false
        }
        return true
    } finally {
        if (zos != null) {
            zos.finish()
            zos.close()
        }
    }
}

/**
 * Zip the files.
 *
 * @param srcFiles The source of files.
 * @param zipFile  The ZIP file.
 * @return `true`: success<br></br>`false`: fail
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun zipFiles(srcFiles: Collection<File>, zipFile: File): Boolean {
    return zipFiles(srcFiles, zipFile, null)
}

/**
 * Zip the files.
 *
 * @param srcFiles The source of files.
 * @param zipFile  The ZIP file.
 * @param comment  The comment.
 * @return `true`: success<br></br>`false`: fail
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun zipFiles(srcFiles: Collection<File>?,
             zipFile: File?,
             comment: String?): Boolean {
    if (srcFiles == null || zipFile == null) return false
    var zos: ZipOutputStream? = null
    try {
        zos = ZipOutputStream(FileOutputStream(zipFile))
        for (srcFile in srcFiles) {
            if (!createZipFile(
                            srcFile,
                            "",
                            zos,
                            comment
                    )
            ) return false
        }
        return true
    } finally {
        if (zos != null) {
            zos.finish()
            zos.close()
        }
    }
}

/**
 * Zip the file.
 *
 * @param srcFilePath The path of source file.
 * @param zipFilePath The path of ZIP file.
 * @return `true`: success<br></br>`false`: fail
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun createZipFile(srcFilePath: String,
                  zipFilePath: String): Boolean {
    return createZipFile(
            getFileByPath(
                    srcFilePath
            ), getFileByPath(zipFilePath), null
    )
}

/**
 * Zip the file.
 *
 * @param srcFilePath The path of source file.
 * @param zipFilePath The path of ZIP file.
 * @param comment     The comment.
 * @return `true`: success<br></br>`false`: fail
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun createZipFile(srcFilePath: String,
                  zipFilePath: String,
                  comment: String): Boolean {
    return createZipFile(
            getFileByPath(
                    srcFilePath
            ), getFileByPath(zipFilePath), comment
    )
}

/**
 * Zip the file.
 *
 * @param srcFile The source of file.
 * @param zipFile The ZIP file.
 * @return `true`: success<br></br>`false`: fail
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun createZipFile(srcFile: File,
                  zipFile: File): Boolean {
    return createZipFile(
            srcFile,
            zipFile,
            null
    )
}

/**
 * Zip the file.
 *
 * @param srcFile The source of file.
 * @param zipFile The ZIP file.
 * @param comment The comment.
 * @return `true`: success<br></br>`false`: fail
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun createZipFile(srcFile: File?,
                  zipFile: File?,
                  comment: String?): Boolean {
    if (srcFile == null || zipFile == null) return false
    var zos: ZipOutputStream? = null
    try {
        zos = ZipOutputStream(FileOutputStream(zipFile))
        return createZipFile(
                srcFile,
                "",
                zos,
                comment
        )
    } finally {
        zos?.close()
    }
}

@Throws(IOException::class)
private fun createZipFile(srcFile: File,
                          rootPath: String,
                          zos: ZipOutputStream,
                          comment: String?): Boolean {
    val modifiedPath = rootPath + (if (isSpace(
                    rootPath
            )
    ) "" else File.separator) + srcFile.name
    if (srcFile.isDirectory) {
        val fileList = srcFile.listFiles()
        if (fileList == null || fileList.isEmpty()) {
            val entry = ZipEntry("$modifiedPath/")
            entry.comment = comment
            zos.putNextEntry(entry)
            zos.closeEntry()
        } else {
            for (file in fileList) {
                if (!createZipFile(
                                file,
                                modifiedPath,
                                zos,
                                comment
                        )
                ) return false
            }
        }
    } else {
        var `is`: InputStream? = null
        try {
            `is` = BufferedInputStream(FileInputStream(srcFile))
            val entry = ZipEntry(modifiedPath)
            entry.comment = comment
            zos.putNextEntry(entry)
            val buffer = ByteArray(BUFFER_LEN)
            val len: Int = `is`.read(buffer, 0, BUFFER_LEN)
            while (len != -1) {
                zos.write(buffer, 0, len)
            }
            zos.closeEntry()
        } finally {
            `is`?.close()
        }
    }
    return true
}

const val BUFFER_LEN = 8192

/**
 * Unzip the file.
 *
 * @param zipFilePath The path of ZIP file.
 * @param destDirPath The path of destination directory.
 * @return the unzipped files
 * @throws IOException if unzip unsuccessfully
 */
@Throws(IOException::class)
fun unzipFile(zipFilePath: String,
              destDirPath: String): List<File>? {
    return unzipFileByKeyword(
            zipFilePath,
            destDirPath,
            null
    )
}

/**
 * Unzip the file.
 *
 * @param zipFile The ZIP file.
 * @param destDir The destination directory.
 * @return the unzipped files
 * @throws IOException if unzip unsuccessfully
 */
@Throws(IOException::class)
fun unzipFile(zipFile: File,
              destDir: File): List<File>? {
    return unzipFileByKeyword(
            zipFile,
            destDir,
            null
    )
}

/**
 * Unzip the file by keyword.
 *
 * @param zipFilePath The path of ZIP file.
 * @param destDirPath The path of destination directory.
 * @param keyword     The keyboard.
 * @return the unzipped files
 * @throws IOException if unzip unsuccessfully
 */
@Throws(IOException::class)
fun unzipFileByKeyword(zipFilePath: String,
                       destDirPath: String,
                       keyword: String?): List<File>? {
    return unzipFileByKeyword(
            getFileByPath(zipFilePath),
            getFileByPath(destDirPath),
            keyword
    )
}

/**
 * Unzip the file by keyword.
 *
 * @param zipFile The ZIP file.
 * @param destDir The destination directory.
 * @param keyword The keyboard.
 * @return the unzipped files
 * @throws IOException if unzip unsuccessfully
 */
@Throws(IOException::class)
fun unzipFileByKeyword(zipFile: File?,
                       destDir: File?,
                       keyword: String?): List<File>? {
    if (zipFile == null || destDir == null) return null
    val files = ArrayList<File>()
    val zf = ZipFile(zipFile)
    val entries = zf.entries()
    if (isSpace(keyword)) {
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            val entryName = entry.name
            if (entryName.contains("../")) {
                entryName.debug("ZipUtils",
                        "it's dangerous!")
                return files
            }
            if (!unzipChildFile(
                            destDir,
                            files,
                            zf,
                            entry,
                            entryName
                    )
            ) return files
        }
    } else {
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            val entryName = entry.name
            if (entryName.contains("../")) {
                entryName.debug("ZipUtils",
                        "it's dangerous!")
                return files
            }
            if (entryName.contains(keyword!!)) {
                if (!unzipChildFile(
                                destDir,
                                files,
                                zf,
                                entry,
                                entryName
                        )
                ) return files
            }
        }
    }
    return files
}


const val KB = 1024
const val MB = KB * KB

@Throws(IOException::class)
private fun unzipChildFile(destDir: File,
                           files: MutableList<File>,
                           zf: ZipFile,
                           entry: ZipEntry,
                           entryName: String): Boolean {
    val filePath = destDir.toString() + File.separator + entryName
    val file = File(filePath)
    files.add(file)
    if (entry.isDirectory) {
        if (!createOrExistsDir(file)) return false
    } else {
        if (!createOrExistsFile(file)) return false
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {
            `in` = BufferedInputStream(zf.getInputStream(entry))
            out = BufferedOutputStream(FileOutputStream(file))
            val buffer = ByteArray(BUFFER_LEN)
            val len: Int = `in`.read(buffer)
            while (len != -1) {
                out.write(buffer, 0, len)
            }
        } finally {
            `in`?.close()
            out?.close()
        }
    }
    return true
}


/**
 * Return the files' path in ZIP file.
 *
 * @param zipFilePath The path of ZIP file.
 * @return the files' path in ZIP file
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun getFilesPath(zipFilePath: String): List<String>? {
    return getFilesPath(
            getFileByPath(
                    zipFilePath
            )
    )
}

/**
 * Return the files' path in ZIP file.
 *
 * @param zipFile The ZIP file.
 * @return the files' path in ZIP file
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun getFilesPath(zipFile: File?): List<String>? {
    if (zipFile == null) return null
    val paths = ArrayList<String>()
    val entries = ZipFile(zipFile).entries()
    while (entries.hasMoreElements()) {
        paths.add((entries.nextElement() as ZipEntry).name)
    }
    return paths
}

/**
 * Return the files' comment in ZIP file.
 *
 * @param zipFilePath The path of ZIP file.
 * @return the files' comment in ZIP file
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun getComments(zipFilePath: String): List<String>? {
    return getComments(
            getFileByPath(
                    zipFilePath
            )
    )
}

/**
 * Return the files' comment in ZIP file.
 *
 * @param zipFile The ZIP file.
 * @return the files' comment in ZIP file
 * @throws IOException if an I/O error has occurred
 */
@Throws(IOException::class)
fun getComments(zipFile: File?): List<String>? {
    if (zipFile == null) return null
    val comments = ArrayList<String>()
    val entries = ZipFile(zipFile).entries()
    while (entries.hasMoreElements()) {
        val entry = entries.nextElement() as ZipEntry
        comments.add(entry.comment)
    }
    return comments
}

private fun createOrExistsDir(file: File?): Boolean {
    return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
}

private fun createOrExistsFile(file: File?): Boolean {
    if (file == null) return false
    if (file.exists()) return file.isFile
    if (!createOrExistsDir(file.parentFile)) return false
    return try {
        file.createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }

}

private fun getFileByPath(filePath: String): File? {
    return if (isSpace(filePath)) null else File(filePath)
}

private fun isSpace(s: String?): Boolean {
    if (s == null) return true
    var i = 0
    val len = s.length
    while (i < len) {
        if (!Character.isWhitespace(s[i])) {
            return false
        }
        ++i
    }
    return true
}