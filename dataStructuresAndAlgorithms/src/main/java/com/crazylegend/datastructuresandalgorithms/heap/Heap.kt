package com.crazylegend.datastructuresandalgorithms.heap

interface Heap<T> : Collection<T> {
    fun peek(): T?
    fun getNthSmallestT(n: T): T?

    fun merge(heap: AbstractHeap<T>)

    fun isMinHeap(): Boolean
}