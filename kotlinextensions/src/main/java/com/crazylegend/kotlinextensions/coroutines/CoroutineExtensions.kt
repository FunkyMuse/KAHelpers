package com.crazylegend.kotlinextensions.coroutines

import kotlinx.coroutines.*


/**
 * Created by hristijan on 5/27/19 to long live and prosper !
 */


fun <T> ioCoroutine(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.IO) {
        block()
    }
}

fun <T> mainCoroutine(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.Main) {
        block()
    }
}

fun <T> defaultCoroutine(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.Default) {
        block()
    }
}

fun <T> unconfinedCoroutine(block: suspend () -> T): Job {
    return GlobalScope.launch(Dispatchers.Unconfined) {
        block()
    }
}

suspend fun <T> withMainContext(block: suspend () -> T): T {
    return withContext(Dispatchers.Main) {
        block()
    }
}

suspend fun <T> withIOContext(block: suspend () -> T) :T {
    return withContext(Dispatchers.IO) {
        block()
    }
}


suspend fun <T> withDefaultContext(block: suspend () -> T) :T {
    return withContext(Dispatchers.Default) {
        block()
    }
}


suspend fun <T> withUnconfinedContext(block: suspend () -> T) :T {
    return withContext(Dispatchers.Unconfined) {
        block()
    }
}
