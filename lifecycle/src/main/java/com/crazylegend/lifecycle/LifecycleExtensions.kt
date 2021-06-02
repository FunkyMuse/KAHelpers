package com.crazylegend.lifecycle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.addRepeatingJob
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * Created by crazy on 1/21/20 to long live and prosper !
 */


fun LifecycleRegistry.setInitialized() {
    currentState = Lifecycle.State.INITIALIZED
}

fun LifecycleRegistry.setCreated() {
    currentState = Lifecycle.State.CREATED
}

fun LifecycleRegistry.setDestroyed() {
    currentState = Lifecycle.State.DESTROYED
}

fun LifecycleRegistry.setResumed() {
    currentState = Lifecycle.State.DESTROYED
}

fun LifecycleRegistry.setStarted() {
    currentState = Lifecycle.State.STARTED
}

fun LifecycleRegistry.firstThreeStages() {
    currentState = Lifecycle.State.INITIALIZED
    currentState = Lifecycle.State.CREATED
    currentState = Lifecycle.State.STARTED
}

fun Fragment.repeatingJobOnStarted(coroutineContext: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED, coroutineContext, block)
}

fun Fragment.repeatingJobOnResumed(coroutineContext: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.addRepeatingJob(Lifecycle.State.RESUMED, coroutineContext, block)
}

fun AppCompatActivity.repeatingJobOnStarted(coroutineContext: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) {
    addRepeatingJob(Lifecycle.State.STARTED, coroutineContext, block)
}

fun AppCompatActivity.repeatingJobOnResumed(coroutineContext: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) {
    addRepeatingJob(Lifecycle.State.RESUMED, coroutineContext, block)
}

