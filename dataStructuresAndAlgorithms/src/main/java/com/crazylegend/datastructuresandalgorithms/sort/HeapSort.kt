package com.crazylegend.datastructuresandalgorithms.sort

import com.crazylegend.datastructuresandalgorithms.swap
import com.crazylegend.datastructuresandalgorithms.swapAt


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
Inventor: J. W. J. Williams
Worst complexity: n*log(n)
Average complexity: n*log(n)
Best complexity: n*log(n)
Space complexity: 1
 */

private fun <T : Comparable<T>> maxHeapify(list: MutableList<T>, index: Int, heapSize: Int) {
    val left = index * 2
    val right = (index * 2) + 1
    var largestIndex: Int = index
    if (left < heapSize && list[left] > list[largestIndex]) largestIndex = left
    if (right < heapSize && list[right] > list[largestIndex]) largestIndex = right
    if (largestIndex != index) {
        list.swap(index, largestIndex)
        maxHeapify(list, largestIndex, heapSize)
    }
}

private fun <T : Comparable<T>> buildMaxHeap(list: MutableList<T>) {
    val length = list.size / 2
    for (i in length downTo 0) maxHeapify(list, i, list.size)
}

fun <T : Comparable<T>> heapSort(list: MutableList<T>) {
    buildMaxHeap(list)
    var heapSize = list.size
    for (i in (list.size - 1) downTo 1) {
        list.swap(0, i)
        maxHeapify(list, 0, --heapSize)
    }
}


fun <T> Array<T>.heapSort(comparator: Comparator<T>) {
    this.heapfy(comparator)
    for (index in this.indices.reversed()) {
        this.swapAt(0, index)
        shiftDown(0, index, comparator)
    }
}

fun <T> Array<T>.heapfy(comparator: Comparator<T>) {

    if (this.isNotEmpty()) {
        (this.size / 2 downTo 0).forEach {
            this.shiftDown(it, this.size, comparator)
        }
    }
}

fun <T> Array<T>.shiftDown(index: Int, upTo: Int, comparator: Comparator<T>) {
    var parent = index
    while (true) {
        val left = leftChildIndex(parent)
        val right = rightChildIndex(parent)
        var candidate = parent
        if (left < upTo && comparator.compare(this[left], this[candidate]) > 0) {
            candidate = left
        }
        if (right < upTo && comparator.compare(this[right], this[candidate]) > 0) {
            candidate = right
        }
        if (candidate == parent) {
            return
        }
        this.swapAt(parent, candidate)
        parent = candidate
    }
}


private fun leftChildIndex(index: Int) = (2 * index) + 1

private fun rightChildIndex(index: Int) = (2 * index) + 2