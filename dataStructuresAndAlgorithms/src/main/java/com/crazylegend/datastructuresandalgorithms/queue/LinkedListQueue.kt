package com.crazylegend.datastructuresandalgorithms.queue

import com.crazylegend.datastructuresandalgorithms.linkedList.LinkedList


class LinkedListQueue<T> : Queue<T> {

    var size = 0
        private set

    /**
     * Space complexity O(N) due to list's size implementation
     */
    private val list = LinkedList<T>()

    /**
     * Adds element at the start of the queue O(1)
     * @return T?
     */
    override fun enqueue(element: T): Boolean {
        list.append(element)
        size++
        return true
    }


    /**
     * Removes the first element of the queue O(1)
     * @return T?
     */
    override fun dequeue(): T? {
        val first = list.firstOrNull()
        return if (list.remove(first)) {
            size--
            first
        } else {
            null
        }
    }

    /**
     * Returns the first element of the queue O(1)
     * @return T?
     */
    override fun peek(): T? = list.firstOrNull()

    override fun toString(): String = list.toString()

    override val count: Int
        get() = size
    override val isEmpty: Boolean
        get() = list.isEmpty()
}