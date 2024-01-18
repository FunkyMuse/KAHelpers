package com.funkymuse.file

import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import java.io.*
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream




fun File.lastModifiedDateTimeAsString(): String = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(lastModified())

val String.fileExtension
    get() = File(this).extension

fun File.mkdirsIfNotExist() = parentFile?.exists() ?: false || parentFile?.mkdirs() ?: false

/**
 * The method is use to prevent a problem, when several threads can try to [.mkdirsIfNotExist] for the same Path simultaneously.
 */
@Synchronized
fun File.mkdirsIfNotExistSynchronized() = mkdirsIfNotExist()

inline fun File.saveFile(runBlockIfOk: () -> Unit, runBlockIfFail: () -> Unit, useMkdirsIfNotExistSynchronized: Boolean) {
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
    return if (!exists()) {
        0
    } else {
        getFiles().sumByLong { it.length() }
    }
}

private inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
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
        val out = ZipOutputStream(
            BufferedOutputStream(
            dest)
        )

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
                  zipFile: File
): Boolean {
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
              destDir: File
): List<File>? {
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
                Log.d("ZipUtils",
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
                Log.d("ZipUtils",
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