package com.funkymuse.datastructuresandalgorithms.queue.priority.comparator

import com.funkymuse.datastructuresandalgorithms.heap.ComparatorHeap
import com.funkymuse.datastructuresandalgorithms.heap.Heap
import com.funkymuse.datastructuresandalgorithms.queue.priority.AbstractPriorityQueue

class ComparatorPriorityQueue<T>(comparator: Comparator<T>) : AbstractPriorityQueue<T>() {
    override val heap: Heap<T> = ComparatorHeap(comparator)

    override val isEmpty: Boolean
        get() = heap.isEmpty
}