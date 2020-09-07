package com.crazylegend.datastructuresandalgorithms.queue


interface Queue<T> {
    fun enqueue(element: T): Boolean

    fun dequeue(): T?

    fun peek(): T?

    val count: Int

    val isEmpty: Boolean

    val isNotEmpty get() = !isEmpty

}