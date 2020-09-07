package com.crazylegend.datastructuresandalgorithms.heap

class ComparatorHeap<T>(private val comparator: Comparator<T>) : AbstractHeap<T>() {

    override fun compare(first: T, second: T) = comparator.compare(first, second)

    companion object {
        fun <T> create(elements: ArrayList<T>, comparator: Comparator<T>): Heap<T> {
            val heap = ComparatorHeap(comparator)
            heap.heapify(elements)
            return heap
        }
    }
}