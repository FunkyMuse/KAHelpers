package com.crazylegend.kotlinextensions.lifeCycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry


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
