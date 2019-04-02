package com.crazylegend.kotlinextensions.compress

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater


/**
 * Created by hristijan on 3/20/19 to long live and prosper !
 */

fun ByteArray.compress(): ByteArray {
    val deflater = Deflater()
    deflater.setInput(this)

    val outputStream = ByteArrayOutputStream(this.size)

    deflater.finish()
    val buffer = ByteArray(1024)
    while (!deflater.finished()) {
        val count = deflater.deflate(buffer) // returns the generated code... index
        outputStream.write(buffer, 0, count)
    }
    outputStream.close()
    val output = outputStream.toByteArray()

    deflater.end()

    return output
}

fun ByteArray.decompress(): ByteArray {
    val inflater = Inflater()
    inflater.setInput(this)

    val outputStream = ByteArrayOutputStream(this.size)
    val buffer = ByteArray(1024)
    while (!inflater.finished()) {
        val count = inflater.inflate(buffer)
        outputStream.write(buffer, 0, count)
    }
    outputStream.close()
    val output = outputStream.toByteArray()

    inflater.end()

    return output
}