package com.funkymuse.datastructuresandalgorithms.sort.quickSort

import com.funkymuse.datastructuresandalgorithms.swapAt


fun <T : Comparable<T>> MutableList<T>.quickSortMedian(low: Int, high: Int) {
    if (low < high) {
        val pivotIndex = medianOfThree(low, high)
        this.swapAt(pivotIndex, high)
        val pivot = partitionLomuto(low, high)
        this.quicksortLomuto(low, pivot - 1)
        this.quicksortLomuto(pivot + 1, high)
    }
}

/**
 * Median of three finds the pivot by taking the median of the first, middle and last
element.
 * @receiver MutableList<T>
 * @param low Int
 * @param high Int
 * @return Int
 */
fun <T : Comparable<T>> MutableList<T>.medianOfThree(low: Int, high: Int): Int {
    val center = (low + high) / 2
    if (this[low] > this[center]) {
        this.swapAt(low, center)
    }
    if (this[low] > this[high]) {
        this.swapAt(low, high)
    }
    if (this[center] > this[high]) {
        this.swapAt(center, high)
    }
    return center
}