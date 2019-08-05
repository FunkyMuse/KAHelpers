package com.crazylegend.kotlinextensions.intent
import android.content.Intent


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */



/**
 * Standard [Intent.getIntExtra] but allows for null. Note that this would treat [Int.MIN_VALUE]
 * as null if you were to pass it
 */
inline fun Intent.getIntExtra(name: String): Int? {
    val value = getIntExtra(name, Int.MIN_VALUE)
    if (value == Int.MIN_VALUE) {
        return null
    }
    return value
}

/**
 * Standard [Intent.getBooleanExtra] but allows for null. Note that this would treat [Int.MIN_VALUE]
 * as null if you were to pass it
 */
inline fun Intent.getFloatExtra(name: String): Float? {
    val value = getFloatExtra(name, Float.MIN_VALUE)
    if (value == Float.MIN_VALUE) {
        return null
    }
    return value
}

/**
 * Standard [Intent.getLongExtra] but allows for null. Note that this would treat [Long.MIN_VALUE]
 * as null if you were to pass it
 */
inline fun Intent.getLongExtra(name: String): Long? {
    val value = getLongExtra(name, Long.MIN_VALUE)
    if (value == Long.MIN_VALUE) {
        return null
    }
    return value
}

/**
 * Standard [Intent.getDoubleExtra] but allows for null. Note that this would treat [Double.MIN_VALUE]
 * as null if you were to pass it
 */
inline fun Intent.getDoubleExtra(name: String): Double? {
    val value = getDoubleExtra(name, Double.MIN_VALUE)
    if (value == Double.MIN_VALUE) {
        return null
    }
    return value
}