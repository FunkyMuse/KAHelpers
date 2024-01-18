package com.funkymuse.kotlinextensions.compress

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream



fun gZipcompress(data: ByteArray): ByteArray {
    val stream = ByteArrayOutputStream(data.size)
    val gzip = GZIPOutputStream(stream)
    gzip.write(data)
    return stream.toByteArray()
}

fun gZipdecompress(path: String): ByteArray {
    val gzip = GZIPInputStream(FileInputStream(path))
    val stream = ByteArrayOutputStream()
    gzip.copyTo(stream)
    return stream.toByteArray()
}

fun gZipdecompress(compressed: ByteArray): ByteArray {
    val gzip = GZIPInputStream(ByteArrayInputStream(compressed))
    val stream = ByteArrayOutputStream()
    gzip.copyTo(stream)
    return stream.toByteArray()
}

