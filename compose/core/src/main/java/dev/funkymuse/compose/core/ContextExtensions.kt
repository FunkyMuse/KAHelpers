package dev.funkymuse.compose.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun Context.getComposeActivity(): Activity {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.getComposeActivity()
        else -> getComposeActivity()
    }
}

fun Context.getComposeComponentActivity(): ComponentActivity = getComposeActivity() as ComponentActivity
