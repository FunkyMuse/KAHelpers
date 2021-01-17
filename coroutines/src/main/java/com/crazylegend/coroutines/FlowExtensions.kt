package com.crazylegend.coroutines

import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*


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

@FlowPreview
@ExperimentalCoroutinesApi
fun TextView.textChanges(skipInitialValue: Boolean = false, debounce: Long = 300L): Flow<CharSequence?> =
        callbackFlow<CharSequence?> {
            val listener = this@textChanges.addTextChangedListener {
                if (!isClosedForSend)
                    offer(it)
            }
            awaitClose {
                removeTextChangedListener(listener)
            }
        }.buffer(Channel.CONFLATED)
                .addSkipInitialValueOnClause(skipInitialValue)
                .debounce(debounce)

fun <T> Flow<T>.addSkipInitialValueOnClause(skipInitialValue: Boolean): Flow<T> {
    if (skipInitialValue) {
        ignoreFirst()
    }
    return this
}
