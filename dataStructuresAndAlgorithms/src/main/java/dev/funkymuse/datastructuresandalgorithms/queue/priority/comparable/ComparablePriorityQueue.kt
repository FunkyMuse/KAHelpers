package dev.funkymuse.datastructuresandalgorithms.queue.priority.comparable

import dev.funkymuse.datastructuresandalgorithms.heap.ComparableHeap
import dev.funkymuse.datastructuresandalgorithms.heap.Heap
import dev.funkymuse.datastructuresandalgorithms.queue.priority.AbstractPriorityQueue

class ComparablePriorityQueue<T : Comparable<T>> : AbstractPriorityQueue<T>() {
    override val heap: Heap<T> = ComparableHeap()

    override val isEmpty: Boolean
        get() = heap.isEmpty
}