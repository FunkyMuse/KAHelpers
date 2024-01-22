package dev.funkymuse.kotlinextensions.viewmodel

import android.content.Context
import androidx.lifecycle.AndroidViewModel

/**
 * Gets the application context from within the android viewmodel
 */
val AndroidViewModel.context: Context
    get() = getApplication()