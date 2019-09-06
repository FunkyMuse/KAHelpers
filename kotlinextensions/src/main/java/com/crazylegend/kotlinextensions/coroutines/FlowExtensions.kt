package com.crazylegend.kotlinextensions.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow


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

