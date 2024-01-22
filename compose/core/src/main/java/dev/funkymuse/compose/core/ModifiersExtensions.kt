package dev.funkymuse.compose.core

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

inline fun <T : Any> Modifier.ifNotNull(value: T?, builder: (T) -> Modifier): Modifier =
    then(if (value != null) builder(value) else Modifier)

inline fun <T : Any> Modifier.ifNull(value: T?, builder: () -> Modifier): Modifier =
    then(if (value == null) builder() else Modifier)

inline fun Modifier.ifTrue(predicate: Boolean, builder: () -> Modifier) =
    then(if (predicate) builder() else Modifier)

inline fun Modifier.ifFalse(predicate: Boolean, builder: () -> Modifier) =
    then(if (!predicate) builder() else Modifier)

inline fun Modifier.debounceClickable(
    debounceInterval: Long = 1000L,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    crossinline onClick: () -> Unit,
): Modifier = composed {
    Modifier.debounceClickable(
        debounceInterval = debounceInterval,
        interactionSource = remember { MutableInteractionSource() },
        indication = LocalIndication.current,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = onClick
    )
}

inline fun Modifier.debounceClickable(
    debounceInterval: Long = 1000L,
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    crossinline onClick: () -> Unit,
): Modifier = composed {
    var lastClickedTime by remember { mutableLongStateOf(0L) }
    clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role
    ) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickedTime > debounceInterval) {
            lastClickedTime = currentTime
            onClick()
        }
    }
}