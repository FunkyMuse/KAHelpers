package com.crazylegend.kotlinextensions



/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

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

