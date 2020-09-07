package com.crazylegend.datastructuresandalgorithms.queue.priority.comparator

import com.crazylegend.datastructuresandalgorithms.queue.priority.AbstractPriorityQueueArrayList
import java.util.*

class ComparatorPriorityQueueArrayList<T>(private val comparator: Comparator<T>) :
        AbstractPriorityQueueArrayList<T>() {

    override val isEmpty: Boolean
        get() = elements.isEmpty()

    override fun sort() {
        Collections.sort(elements, comparator)
    }
}