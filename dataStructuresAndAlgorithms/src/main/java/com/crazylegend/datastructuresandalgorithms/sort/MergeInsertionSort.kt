package com.crazylegend.datastructuresandalgorithms.sort


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
 * Merge-Insertion sort is a sorting algorithm using the principle that for
 * smaller sets of data, insertion sort is more efficient than merge sort.
 * It uses a threshold. If the list's size is below the threshold, insertion sort will be used.
 *
 * Principle:
 *      Use insertion sort for smaller lists below the threshold size, otherwise
 *      use merge sort.
 *
 * Time complexity: O(n log n) on average.
 *
 * Space complexity: O(n) auxiliary for merging.
 */
fun mergeInsertionSort(list: MutableList<Int>, insertionThreshold: Int = 8) {
    mergeInsertionSort(list, 0, list.size - 1, insertionThreshold)
}

private fun mergeInsertionSort(list: MutableList<Int>, start: Int, end: Int, insertionThreshold: Int = 8) {
    if (end > start) {
        if ((end - start) > insertionThreshold) {
            val mid = (end + start) / 2
            mergeInsertionSort(list, start, mid)
            mergeInsertionSort(list, mid + 1, end)
            merge(list, start, mid, end)
        } else {
            insertionSort(list, start, end)
        }
    }
}


private fun <T : Comparable<T>> insertionSort(list: MutableList<T>, start: Int = 0, end: Int = list.size) {
    for (j in (start + 1)..end) {
        val key = list[j]
        var i = j - 1
        while (i >= start && list[i] > key) {
            list[i + 1] = list[i]
            i -= 1
        }
        list[i + 1] = key
    }
}

private fun merge(list: MutableList<Int>, start: Int, mid: Int, end: Int) {
    val numLeft = mid - start + 1
    val numRight = end - mid
    val leftArray = IntArray(numLeft + 1)
    val rightArray = IntArray(numRight + 1)
    for (i in 1..numLeft) {
        leftArray[i - 1] = list[start + i - 1]
    }
    for (i in 1..numRight) {
        rightArray[i - 1] = list[mid + i]
    }
    leftArray[numLeft] = Int.MAX_VALUE
    rightArray[numRight] = Int.MAX_VALUE
    var i = 0
    var j = 0
    for (k in start..end) {
        list[k] = if (leftArray[i] < rightArray[j]) leftArray[i++] else rightArray[j++]
    }
}