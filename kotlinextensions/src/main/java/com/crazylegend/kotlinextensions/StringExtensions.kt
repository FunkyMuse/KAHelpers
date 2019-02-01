package com.crazylegend.kotlinextensions

import android.util.Base64


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
fun String.decodeBase64(): String {

    return Base64.decode(this, Base64.DEFAULT).toString(Charsets.UTF_8)
}

fun String.encodeBase64(): String {
    return Base64.encodeToString(this.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
}

fun String?.isNotNullOrEmpty():Boolean {
    return !this.isNullOrEmpty()
}