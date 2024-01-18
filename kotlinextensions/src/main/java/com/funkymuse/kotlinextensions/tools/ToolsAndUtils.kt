package com.funkymuse.kotlinextensions.tools

import kotlin.reflect.KClass

/**
 * Wraps the [kotlin.checkNotNull] to avoid IllegalStateExceptions. Just returns false instead of
 * the Exception like it is implemented in the native kotlin implementation.
 */
fun <T : Any> isNotNull(value: T?): Boolean {
    return try {
        checkNotNull(value)
        true
    } catch (e: IllegalStateException) {
        false
    }
}

/**
 * [kotlin.let] similar function with two parameter support.
 */
inline fun <T1, T2, R> multiLet(value1: T1?, value2: T2?, block: (T1, T2) -> R?): R? {
    if (value1 != null && value2 != null) {
        return block(value1, value2)
    }

    return null
}

/**
 * [kotlin.let] similar function with three parameter support.
 */
inline fun <T1, T2, T3, R> multiLet(value1: T1?, value2: T2?, value3: T3?, block: (T1, T2, T3) -> R?): R? {
    if (value1 != null && value2 != null && value3 != null) {
        return block(value1, value2, value3)
    }

    return null
}

/**
 * Take multiple Exceptions to catch and call the [catchBlock] if it gets thrown. If the thrown Exception is not in the list
 * it will be just forwarded.
 */
inline fun multiCatch(runThis: () -> Unit, catchBlock: (Throwable) -> Unit, vararg exceptions: KClass<out Throwable>) {
    try {
        runThis()
    } catch (exception: Exception) {
        val contains = exceptions.find {
            it.isInstance(exception)
        }
        if (contains != null) catchBlock(exception)
        else throw exception
    }
}
