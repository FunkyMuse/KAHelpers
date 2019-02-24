package com.crazylegend.kotlinextensions

import android.util.Log


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

var logTag = ""

fun doLogException(e: Exception) {
    Log.e(logTag, "Exception", e)
}

fun doLog(o: Any) {
    try {
        Log.d(logTag, o.toString())
    } catch (e: Exception) {
        doLogException(e)
    }

}

val <T> T.isNull : Boolean get() {
    return this == null
}

val <T> T.isNotNull : Boolean get() {
    return this != null
}

/**
 * Debug mode code
 */
inline fun debugMode(block : () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

