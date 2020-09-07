package com.crazylegend.datastructuresandalgorithms.queue.priority.comparator

import com.crazylegend.datastructuresandalgorithms.heap.ComparatorHeap
import com.crazylegend.datastructuresandalgorithms.heap.Heap
import com.crazylegend.datastructuresandalgorithms.queue.priority.AbstractPriorityQueue

class ComparatorPriorityQueue<T>(comparator: Comparator<T>) : AbstractPriorityQueue<T>() {
    override val heap: Heap<T> = ComparatorHeap(comparator)

    override val isEmpty: Boolean
        get() = heap.isEmpty
}