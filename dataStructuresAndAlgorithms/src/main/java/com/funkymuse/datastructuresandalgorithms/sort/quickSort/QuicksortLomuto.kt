package com.funkymuse.datastructuresandalgorithms.sort.quickSort

import com.funkymuse.datastructuresandalgorithms.swapAt


/**
 * Lomuto’s partitioning chooses the last element as the pivot.
 * @receiver MutableList<T>
 * @param low Int
 * @param high Int
 */
fun <T : Comparable<T>> MutableList<T>.quicksortLomuto(low: Int, high: Int) {
    if (low < high) {
        val pivot = this.partitionLomuto(low, high)
        this.quicksortLomuto(low, pivot - 1)
        this.quicksortLomuto(pivot + 1, high)
    }
}

fun <T : Comparable<T>> MutableList<T>.partitionLomuto(
        low: Int,
        high: Int): Int {
    val pivot = this[high]

    var i = low
    for (j in low until high) {
        if (this[j] <= pivot) {
            this.swapAt(i, j)
            i += 1
        }
        this.subList(low, i)
    }
    this.swapAt(i, high)
    return i
}

