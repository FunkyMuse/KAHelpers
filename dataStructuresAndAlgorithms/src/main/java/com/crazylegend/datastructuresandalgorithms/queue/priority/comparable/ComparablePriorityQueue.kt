package com.crazylegend.datastructuresandalgorithms.queue.priority.comparable

import com.crazylegend.datastructuresandalgorithms.heap.ComparableHeap
import com.crazylegend.datastructuresandalgorithms.heap.Heap
import com.crazylegend.datastructuresandalgorithms.queue.priority.AbstractPriorityQueue

class ComparablePriorityQueue<T : Comparable<T>> : AbstractPriorityQueue<T>() {
    override val heap: Heap<T> = ComparableHeap()

    override val isEmpty: Boolean
        get() = heap.isEmpty
}