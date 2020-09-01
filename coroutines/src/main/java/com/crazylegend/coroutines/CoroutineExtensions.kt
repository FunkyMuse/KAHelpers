package com.crazylegend.coroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


/**
 * Created by hristijan on 5/27/19 to long live and prosper !
 */

suspend inline fun <T, R> T.onMain(crossinline block: (T) -> R): R {
    return withContext(mainDispatcher) { this@onMain.let(block) }
}

suspend inline fun <T> onMain(crossinline block: CoroutineScope.() -> T): T {
    return withContext(mainDispatcher) { block.invoke(this@withContext) }
}

suspend inline fun <T, R> T.onDefault(crossinline block: (T) -> R): R {
    return withContext(defaultDispatcher) { this@onDefault.let(block) }
}

suspend inline fun <T, R> T.nonCancellable(crossinline block: (T) -> R): R {
    return withContext(NonCancellable) { this@nonCancellable.let(block) }
}

suspend inline fun <T> onDefault(crossinline block: CoroutineScope.() -> T): T {
    return withContext(defaultDispatcher) { block.invoke(this@withContext) }
}

suspend inline fun <T, R> T.onIO(crossinline block: (T) -> R): R {
    return withContext(ioDispatcher) { this@onIO.let(block) }
}

suspend inline fun <T> onIO(crossinline block: CoroutineScope.() -> T): T {
    return withContext(ioDispatcher) { block.invoke(this@withContext) }
}

val mainDispatcher = Dispatchers.Main
val defaultDispatcher = Dispatchers.Default
val unconfinedDispatcher = Dispatchers.Unconfined
val ioDispatcher = Dispatchers.IO


fun <T> ioCoroutineGlobal(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, block: suspend () -> T): Job {
    return GlobalScope.launch(ioDispatcher, coroutineStart) {
        block()
    }
}

fun <T> mainCoroutineGlobal(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, block: suspend () -> T): Job {
    return GlobalScope.launch(mainDispatcher, coroutineStart) {
        block()
    }
}

fun <T> defaultCoroutineGlobal(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, block: suspend () -> T): Job {
    return GlobalScope.launch(defaultDispatcher, coroutineStart) {
        block()
    }
}

fun <T> unconfinedCoroutineGlobal(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, block: suspend () -> T): Job {
    return GlobalScope.launch(unconfinedDispatcher, coroutineStart) {
        block()
    }
}

suspend fun <T> withMainContext(block: suspend () -> T): T {
    return withContext(mainDispatcher) {
        block()
    }
}

suspend fun <T> withIOContext(block: suspend () -> T): T {
    return withContext(ioDispatcher) {
        block()
    }
}


suspend fun <T> withDefaultContext(block: suspend () -> T): T {
    return withContext(defaultDispatcher) {
        block()
    }
}


suspend fun <T> withUnconfinedContext(block: suspend () -> T): T {
    return withContext(Dispatchers.Unconfined) {
        block()
    }
}

suspend fun <T> withNonCancellableContext(block: suspend () -> T): T {
    return withContext(NonCancellable) {
        block()
    }
}


/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelIOCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(ioDispatcher, coroutineStart) {
        action(this)
    }
}


/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelMainCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(mainDispatcher, coroutineStart) {
        action(this)
    }
}


/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelDefaultCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(defaultDispatcher, coroutineStart) {
        action(this)
    }
}

/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelUnconfinedCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(unconfinedDispatcher, coroutineStart) {
        action(this)
    }
}

/**
 *
 * @receiver ViewModel
 * @param action SuspendFunction0<Unit>
 * @return Job
 */
fun ViewModel.viewModelNonCancellableCoroutine(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, action: suspend (scope: CoroutineScope) -> Unit = {}): Job {
    return viewModelScope.launch(NonCancellable, coroutineStart) {
        action(this)
    }
}


inline fun CoroutineScope.main(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit) {
    launch(mainDispatcher, coroutineStart) {
        function()
    }
}

inline fun CoroutineScope.io(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit): Job {
    return launch(ioDispatcher, coroutineStart) {
        function()
    }
}

inline fun CoroutineScope.default(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit): Job {
    return launch(defaultDispatcher, coroutineStart) {
        function()
    }
}

inline fun CoroutineScope.unconfined(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit): Job {
    return launch(unconfinedDispatcher, coroutineStart) {
        function()
    }
}

inline fun CoroutineScope.nonCancellable(coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, crossinline function: suspend () -> Unit): Job {
    return launch(NonCancellable, coroutineStart) {
        function()
    }
}

suspend fun ByteArray.toBitmapSuspend(): Bitmap? {
    return onIO {
        return@onIO tryOrNull { BitmapFactory.decodeByteArray(this, 0, size) }
    }
}


internal inline fun <T> tryOrNull(block: () -> T): T? = try {
    block()
} catch (e: Exception) {
    null
}
