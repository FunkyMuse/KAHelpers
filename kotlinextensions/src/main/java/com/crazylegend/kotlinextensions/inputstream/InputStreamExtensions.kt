package com.crazylegend.kotlinextensions.inputstream

import java.io.InputStream
import java.nio.charset.Charset


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String = bufferedReader(charset).use { it.readText() }