package com.funkymuse.coroutines

import kotlinx.coroutines.sync.Semaphore

//https://github.com/Kotlin/kotlinx.coroutines/issues/2886
class ExactlyOnceEventBus<T> {
    private val buffer = ArrayDeque<T>()
    private val semaphore = Semaphore(Int.MAX_VALUE, Int.MAX_VALUE)

    fun send(event: T) {
        synchronized(buffer) { buffer.add(event) }
        semaphore.release()
    }

    suspend fun receive(): T {
        semaphore.acquire()
        return synchronized(buffer) { buffer.removeFirst() }
    }
}