package dev.funkymuse.coroutines

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch



suspend fun doParallel(vararg blocks: suspend () -> Any) = coroutineScope {
    blocks
        .map { async { it() } }
        .forEach { it.await() }
}

suspend fun <T> doParallelWithResult(vararg blocks: suspend () -> T) = coroutineScope {
    val result = mutableListOf<T>()
    blocks
        .map { async { it() } }
        .forEach { result.add(it.await()) }

    return@coroutineScope result
}


fun View.setOnClickCoroutine(owner: LifecycleOwner, listener: suspend (view: View) -> Unit) =
    this.setOnClickListener { owner.lifecycleScope.launch { listener(it) } }