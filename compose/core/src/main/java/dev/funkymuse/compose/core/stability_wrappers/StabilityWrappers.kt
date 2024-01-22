package dev.funkymuse.compose.core.stability_wrappers

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
class StableHolder<T>(val item: T) {
    operator fun component1(): T = item
}

@Immutable
class ImmutableHolder<T>(val item: T) {
    operator fun component1(): T = item
}

fun <T> T.asStable() = StableHolder(this)
val <T> T.asStable get() = StableHolder(this)
fun <T> T.asImmutable() = ImmutableHolder(this)
val <T> T.asImmutable get() = ImmutableHolder(this)