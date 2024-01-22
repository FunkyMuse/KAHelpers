package dev.funkymuse.compose.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.test.junit4.ComposeContentTestRule



internal fun ComposeContentTestRule.setContentWithTestViewConfiguration(
    composable: @Composable () -> Unit
) {
    this.setContent {
        CompositionLocalProvider(LocalViewConfiguration provides fakeViewConfiguration) {
            composable()
        }
    }
}


private val fakeViewConfiguration = object : ViewConfiguration {
    override val longPressTimeoutMillis: Long
        get() = 500L
    override val doubleTapTimeoutMillis: Long
        get() = 300L
    override val doubleTapMinTimeMillis: Long
        get() = 40L
    override val touchSlop: Float
        get() = TestTouchSlop
}

internal val TestTouchSlop = 18f