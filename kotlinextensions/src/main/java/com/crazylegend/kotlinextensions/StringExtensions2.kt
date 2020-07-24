package com.crazylegend.kotlinextensions


inline fun String.ifIsNullOrEmpty(action: () -> Unit) {
    if (isNullOrEmpty()) action()
}

inline fun String.ifIsNotNullOrEmpty(action: () -> Unit) {
    if (!isNullOrEmpty()) action()
}

