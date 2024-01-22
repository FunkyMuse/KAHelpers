package dev.funkymuse.kotlinextensions

import android.widget.ArrayAdapter




operator fun <T> ArrayAdapter<T>.get(position: Int): T? = getItem(position)
