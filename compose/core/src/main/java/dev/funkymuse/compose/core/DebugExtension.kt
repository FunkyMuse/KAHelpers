package dev.funkymuse.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember

@PublishedApi
internal class LogCompositionsRef(var value: Int)


@Composable
inline fun LogCompositions(
    isDebugEnabled: Boolean = true,
    log: (count: Int) -> Unit
) {
    if (isDebugEnabled) {
        val logCompositionsRef = remember { LogCompositionsRef(0) }
        SideEffect { logCompositionsRef.value++ }
        log(logCompositionsRef.value)
    }
}