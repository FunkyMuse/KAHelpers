package dev.funkymuse.datastructuresandalgorithms.memoization

fun <T, R> ((T) -> R).memoize(): (T) -> R = Memoize(this)
