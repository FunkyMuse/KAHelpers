package com.funkymuse.kotlinextensions.inputstream

import java.io.InputStream
import java.nio.charset.Charset

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String = bufferedReader(charset).use { it.readText() }
fun InputStream.readBytesAndClose(): ByteArray = bufferedReader().use { readBytes() }