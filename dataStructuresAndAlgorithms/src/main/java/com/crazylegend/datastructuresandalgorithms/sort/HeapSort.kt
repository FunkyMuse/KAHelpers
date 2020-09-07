package com.crazylegend.datastructuresandalgorithms.sort

import com.crazylegend.datastructuresandalgorithms.swap


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