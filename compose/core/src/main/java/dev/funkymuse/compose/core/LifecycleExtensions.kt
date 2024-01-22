package dev.funkymuse.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.compose.LifecycleEventEffect

@Composable
fun AddLifecycleObserver(observer: LifecycleObserver) {
    val owner = lifecycleOwner.lifecycle
    DisposableEffect(owner) {
        owner.addObserver(observer)
        onDispose {
            owner.removeObserver(observer)
        }
    }
}

@Composable
fun <OWNER : LifecycleObserver> OWNER.ObserveLifecycle(lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@ObserveLifecycle)
        onDispose {
            lifecycle.removeObserver(this@ObserveLifecycle)
        }
    }
}

@Composable
fun <T : Any> onEventValue(event: Lifecycle.Event = Lifecycle.Event.ON_START, value: () -> T): T {
    val rememberLatestUpdateState by rememberUpdatedState(newValue = value)
    var rememberedValue by remember { mutableStateOf(value()) }
    LifecycleEventEffect(event = event) {
        rememberedValue = rememberLatestUpdateState()
    }
    return rememberedValue
}