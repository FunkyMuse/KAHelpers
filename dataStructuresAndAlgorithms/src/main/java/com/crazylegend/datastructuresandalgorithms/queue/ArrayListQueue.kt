package com.crazylegend.datastructuresandalgorithms.queue


class ArrayListQueue<T> : Queue<T> {

    /**
     * Space complexity O(N) due to list implementation
     */
    private val list = arrayListOf<T>()

    /**
     * Inserts item at the end of the queue O(1)
     * @param element T
     * @return Boolean
     */
    override fun enqueue(element: T): Boolean = list.add(element)

    /**
     * Removes the first element of the queue O(N) cause shifts the element for size-1 places on removal
     * @return T?
     */
    override fun dequeue(): T? = list.removeFirstOrNull()

    /**
     * Returns the first element of the queue O(1)
     * @return T?
     */
    override fun peek(): T? = list.firstOrNull()

    override val count: Int
        get() = list.size
    override val isEmpty: Boolean
        get() = list.isEmpty()

    override fun toString() = list.toString()
}