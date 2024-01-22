package dev.funkymuse.compose.core.m3

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

val contentColor: Color
    @Composable get() {
        return LocalContentColor.current
    }

val textStyle: TextStyle
    @Composable get() {
        return LocalTextStyle.current
    }