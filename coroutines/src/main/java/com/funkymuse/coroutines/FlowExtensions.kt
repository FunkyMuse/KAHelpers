package com.funkymuse.coroutines

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flow


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

fun EditText.textChanges(skipInitialValue: Boolean = false, debounce: Long = 300L): Flow<CharSequence?> =
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
        trySend(element)
    }
}

suspend fun <E> ProducerScope<E>.sendIfNotClosed(element: E) {
    if (!isClosedForSend) {
        send(element)
    }
}