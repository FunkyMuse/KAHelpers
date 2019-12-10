package com.crazylegend.kotlinextensions.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform


/**
 * Created by hristijan on 9/6/19 to long live and prosper !
 */

class AbortCollectException : CancellationException()


fun <T> Flow<T>.repeat(): Flow<T> = flow {
    try {
        collect {
            emit(it)
            throw AbortCollectException()
        }
    } catch (e: AbortCollectException) {
        repeat().collect {
            emit(it)
        }
    }
}

/**
 * Flow transformation that ignores the first element emitted by the original Flow.
 */
fun <T> Flow<T>.ignoreFirst(): Flow<T> {
    var firstElement = true
    return transform { value ->
        if (firstElement) {
            firstElement = false
            return@transform
        } else {
            return@transform emit(value)
        }
    }
}
