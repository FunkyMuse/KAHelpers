package com.funkymuse.intent

import android.content.Intent





/**
 * Standard [Intent.getIntExtra] but allows for null. Note that this would treat [Int.MIN_VALUE]
 * as null if you were to pass it
 */
fun Intent.getIntExtra(name: String): Int? {
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
fun Intent.getFloatExtra(name: String): Float? {
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
fun Intent.getLongExtra(name: String): Long? {
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
fun Intent.getDoubleExtra(name: String): Double? {
    val value = getDoubleExtra(name, Double.MIN_VALUE)
    if (value == Double.MIN_VALUE) {
        return null
    }
    return value
}