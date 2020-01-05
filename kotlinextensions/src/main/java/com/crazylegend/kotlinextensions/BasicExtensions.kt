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

inline val isDebugMode get() = BuildConfig.DEBUG


fun <T : Any> T?.orElse(item: T) =
        this ?: item

fun <T : Any> T?.or(item: T?) =
        this ?: item


fun <T : CharSequence> T?.orElse(item: T) =
        if (this != null && isNotBlank()) this else item

fun <T : CharSequence> T?.or(item: T?) =
        if (this != null && isNotBlank()) this else item


fun Byte?.orElse(byte: Byte) =
        if (this != null && this != 0.toByte()) this else byte

fun Byte?.or(byte: Byte?) =
        if (this != null && this != 0.toByte()) this else byte


fun Short?.orElse(short: Short) =
        if (this != null && this != 0.toShort()) this else short

fun Short?.or(short: Short?) =
        if (this != null && this != 0.toShort()) this else short


fun Int?.orElse(int : Int) =
        if (this != null && this != 0) this else int

fun Int?.or(int : Int?) =
        if (this != null && this != 0) this else int


fun Long?.orElse(long : Long) =
        if (this != null && this != 0L) this else long

fun Long?.or(long : Long?) =
        if (this != null && this != 0L) this else long


fun Float?.orElse(float : Float) =
        if (this != null && this != 0f) this else float

fun Float?.or(float : Float?) =
        if (this != null && this != 0f) this else float


fun Double?.orElse(double : Double) =
        if (this != null && this != 0.toDouble()) this else double

fun Double?.or(double : Double?) =
        if (this != null && this != 0.toDouble()) this else double


fun <T> List<T>?.orElse(list : List<T>) =
        if (this != null && isNotEmpty()) this else list

fun <T> List<T>?.or(list : List<T>?) =
        if (this != null && isNotEmpty()) this else list


fun <K, V> Map<K, V>?.orElse(map : Map<K, V>) =
        if (this != null && isNotEmpty()) this else map

fun <K, V> Map<K, V>?.or(map : Map<K, V>?) =
        if (this != null && isNotEmpty()) this else map

fun infiniteLoop(action: () -> Unit) {
    while (true) {
        action()
    }
}