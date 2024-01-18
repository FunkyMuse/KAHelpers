package com.funkymuse.datastructuresandalgorithms.heap

interface Collection<T> {
    val count: Int


    val isEmpty get() = count == 0

    val isNotEmpty get() = !isEmpty

    fun insert(element: T)
    fun remove(): T?
    fun removeAt(index: Int): T?
}