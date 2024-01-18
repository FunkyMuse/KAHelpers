package com.funkymuse.datastructuresandalgorithms.queue.priority.comparator

import com.funkymuse.datastructuresandalgorithms.queue.priority.AbstractPriorityQueueArrayList
import java.util.Collections

class ComparatorPriorityQueueArrayList<T>(private val comparator: Comparator<T>) :
        AbstractPriorityQueueArrayList<T>() {

    override val isEmpty: Boolean
        get() = elements.isEmpty()

    override fun sort() {
        Collections.sort(elements, comparator)
    }
}