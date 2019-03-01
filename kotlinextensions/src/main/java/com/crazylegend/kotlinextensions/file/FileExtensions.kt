package com.crazylegend.kotlinextensions.file

import android.graphics.BitmapFactory
import java.io.*
import java.nio.channels.FileChannel


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