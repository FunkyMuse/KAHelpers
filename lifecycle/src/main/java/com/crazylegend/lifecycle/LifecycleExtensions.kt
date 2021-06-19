package com.crazylegend.lifecycle

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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

fun Fragment.repeatingJobOnStarted(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

fun Fragment.repeatingJobOnResumed(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED, block)
    }
}

fun AppCompatActivity.repeatingJobOnStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

fun AppCompatActivity.repeatingJobOnResumed(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.RESUMED, block) }
}
