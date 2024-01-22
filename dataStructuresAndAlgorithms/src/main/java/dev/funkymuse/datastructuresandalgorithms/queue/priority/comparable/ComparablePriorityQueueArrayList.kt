package dev.funkymuse.datastructuresandalgorithms.queue.priority.comparable

import dev.funkymuse.datastructuresandalgorithms.queue.priority.AbstractPriorityQueueArrayList

class ComparablePriorityQueueArrayList<T : Comparable<T>> :
        AbstractPriorityQueueArrayList<T>() {

    override val isEmpty: Boolean
        get() = elements.isEmpty()

    override fun sort() {
        elements.sort()
    }
}