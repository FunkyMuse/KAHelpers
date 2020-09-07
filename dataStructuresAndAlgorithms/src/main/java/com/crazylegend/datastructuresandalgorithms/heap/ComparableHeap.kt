package com.crazylegend.datastructuresandalgorithms.heap

class ComparableHeap<T : Comparable<T>> : AbstractHeap<T>() {

    override fun compare(first: T, second: T) = first.compareTo(second)

    companion object {
        fun <Element : Comparable<Element>> create(elements: ArrayList<Element>): Heap<Element> {
            val heap = ComparableHeap<Element>()
            heap.heapify(elements)
            return heap
        }
    }

}