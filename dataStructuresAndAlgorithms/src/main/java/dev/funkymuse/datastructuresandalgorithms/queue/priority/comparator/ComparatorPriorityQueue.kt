package dev.funkymuse.datastructuresandalgorithms.queue.priority.comparator

import dev.funkymuse.datastructuresandalgorithms.heap.ComparatorHeap
import dev.funkymuse.datastructuresandalgorithms.heap.Heap
import dev.funkymuse.datastructuresandalgorithms.queue.priority.AbstractPriorityQueue

class ComparatorPriorityQueue<T>(comparator: Comparator<T>) : AbstractPriorityQueue<T>() {
    override val heap: Heap<T> = ComparatorHeap(comparator)

    override val isEmpty: Boolean
        get() = heap.isEmpty
}