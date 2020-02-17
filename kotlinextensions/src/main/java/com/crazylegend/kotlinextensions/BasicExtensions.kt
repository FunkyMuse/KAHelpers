package com.crazylegend.kotlinextensions


/**
 * Created by Hristijan on 2/4/19 to long live and prosper !
 */

val <T> T.isNull: Boolean
    get() {
        return this == null
    }

val <T> T.isNotNull: Boolean
    get() {
        return this != null
    }

/**
 * Debug mode code
 */
inline fun debugMode(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

inline val isInDebugMode get() = BuildConfig.DEBUG


fun <T : Any> T?.orElse(item: T) =
        this ?: item

fun <T : Any> T?.or(item: T?) =
        this ?: item


fun <T : CharSequence> T?.orElse(item: T) =
        if (this != null && isNotBlank()) this else item

fun <T : CharSequence> T?.or(item: T?) =
        if (this != null && isNotBlank()) this else item

fun <T: Number> T?.orElse(number: T) =
        if (this != null && this != 0) this else number

fun <T: Number> T?.or(number: T?) =
        if (this != null && this == 0) this else number

fun <T> List<T>?.orElse(list: List<T>) =
        if (this != null && isNotEmpty()) this else list

fun <T> List<T>?.or(list: List<T>?) =
        if (this != null && isNotEmpty()) this else list


fun <K, V> Map<K, V>?.orElse(map: Map<K, V>) =
        if (this != null && isNotEmpty()) this else map

fun <K, V> Map<K, V>?.or(map: Map<K, V>?) =
        if (this != null && isNotEmpty()) this else map

fun infiniteLoop(action: () -> Unit) {
    while (true) {
        action()
    }
}