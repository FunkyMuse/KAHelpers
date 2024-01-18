package com.funkymuse.datastructuresandalgorithms.queue.priority.comparable

import com.funkymuse.datastructuresandalgorithms.heap.ComparableHeap
import com.funkymuse.datastructuresandalgorithms.heap.Heap
import com.funkymuse.datastructuresandalgorithms.queue.priority.AbstractPriorityQueue

class ComparablePriorityQueue<T : Comparable<T>> : AbstractPriorityQueue<T>() {
    override val heap: Heap<T> = ComparableHeap()

    override val isEmpty: Boolean
        get() = heap.isEmpty
}