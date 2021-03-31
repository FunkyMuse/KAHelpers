package com.crazylegend.coroutines

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ProducerScope
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

fun TextView.textChanges(skipInitialValue: Boolean = false, debounce: Long = 300L): Flow<CharSequence?> =
        callbackFlow<CharSequence?> {
            val listener = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    offerIfNotClosed(s)
                }

                override fun afterTextChanged(s: Editable?) {}
            }
            this@textChanges.addTextChangedListener(listener)
            awaitClose {
                removeTextChangedListener(listener)
            }
        }.buffer(Channel.CONFLATED)
                .drop(dropInitialValueIfSkipped(skipInitialValue))
                .debounce(debounce)

private fun dropInitialValueIfSkipped(skipInitialValue: Boolean) = if (skipInitialValue) 1 else 0

fun <E> ProducerScope<E>.offerIfNotClosed(element: E) {
    if (!isClosedForSend) {
        offer(element)
    }
}

suspend fun <E> ProducerScope<E>.sendIfNotClosed(element: E) {
    if (!isClosedForSend) {
        send(element)
    }
}