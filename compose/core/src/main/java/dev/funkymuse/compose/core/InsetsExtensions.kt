package dev.funkymuse.compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun rememberWindowInsetsController(): WindowInsetsControllerCompat {
    val componentActivity = LocalContext.current.getComposeComponentActivity()
    val view = LocalView.current
    return remember { WindowCompat.getInsetsController(componentActivity.window, view) }
}

