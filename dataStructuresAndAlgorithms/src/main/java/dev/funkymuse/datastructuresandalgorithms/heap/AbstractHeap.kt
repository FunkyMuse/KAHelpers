package dev.funkymuse.datastructuresandalgorithms.heap

import java.util.Collections

abstract class AbstractHeap<T> : Heap<T> {
    abstract fun compare(first: T, second: T): Int

    var elements: ArrayList<T> = ArrayList()

    override fun peek(): T? = elements.firstOrNull()


    private fun leftChildIndex(index: Int) = (2 * index) + 1
    private fun rightChildIndex(index: Int) = (2 * index) + 2
    private fun parentIndex(index: Int) = (index - 1) / 2


    override fun insert(element: T) {
        elements.add(element)
        shiftUp(count - 1)
    }

    private fun shiftUp(index: Int) {
        var child = index
        var parent = parentIndex(child)
        while (child > 0 && compare(elements[child], elements[parent]) > 0) {
            Collections.swap(elements, child, parent)
            child = parent
            parent = parentIndex(child)
        }
    }

    override fun remove(): T? {
        if (isEmpty) return null

        Collections.swap(elements, 0, count - 1)
        val item = elements.removeAt(count - 1)
        shiftDown(0)
        return item
    }

    private fun shiftDown(index: Int) {
        var parent = index
        while (true) {
            val left = leftChildIndex(parent)
            val right = rightChildIndex(parent)
            var candidate = parent
            if (left < count && compare(elements[left], elements[candidate]) > 0) {
                candidate = left
            }

            if (right < count && compare(elements[right], elements[candidate]) > 0) {
                candidate = right
            }

            if (candidate == parent) {
                return
            }
            Collections.swap(elements, parent, candidate) // 8
            parent = candidate
        }
    }

    override fun removeAt(index: Int): T? =
            when {
                index >= count -> null
                index == count - 1 -> elements.removeAt(index)
                else -> {
                    Collections.swap(elements, index, count - 1)
                    val item = elements.removeAt(count - 1)
                    shiftDown(index)
                    shiftUp(index)
                    item
                }
            }

    private fun index(element: T, i: Int): Int? {
        if (i >= count) {
            return null
        }
        if (compare(element, elements[i]) > 0) {
            return null
        }
        if (element == elements[i]) {
            return i
        }
        val leftChildIndex = index(element, leftChildIndex(i))
        if (leftChildIndex != null) return leftChildIndex
        val rightChildIndex = index(element, rightChildIndex(i))
        if (rightChildIndex != null) return rightChildIndex
        return null
    }

    protected fun heapify(values: ArrayList<T>) {
        elements = values
        if (elements.isNotEmpty()) {
            (count / 2 downTo 0).forEach {
                shiftDown(it)
            }
        }
    }

    override fun getNthSmallestT(n: T): T? {
        var current = 1
        while (!isEmpty) {
            val element = remove()
            if (current == n) {
                return element
            }
            current += 1
        }
        return null
    }

    override fun merge(heap: AbstractHeap<T>) {
        elements.addAll(heap.elements)
        buildHeap()
    }

    private fun buildHeap() {
        if (elements.isNotEmpty()) {
            (count / 2 downTo 0).forEach {
                shiftDown(it)
            }
        }
    }

    override fun isMinHeap(): Boolean {
        if (isEmpty) return true
        (count / 2 - 1 downTo 0).forEach {
            val left = leftChildIndex(it)
            val right = rightChildIndex(it)
            if (left < count &&
                    compare(elements[left], elements[it]) < 0) {
                return false
            }
            if (right < count
                    && compare(elements[right], elements[it]) < 0) {
                return false
            }
        }
        return true
    }


    override val count: Int
        get() = elements.count()
}