package com.funkymuse.datastructuresandalgorithms.search

/**
 * Binary stringSearch, assumes that the list is sorted.
 * Splits the list in half with every single iteration, bringing the worst case running time to O(log n)
 *
 * Time complexity per case:
 *      Best - O(1), constant. In case the element is in the very middle.
 *      Average, Worst - O(log n), logarithmic.
 */
fun <T : Comparable<T>> binarySearch(list: List<T>, value: T, startIndex: Int = 0, endIndex: Int = list.size - 1): Int {
    if (endIndex >= startIndex) {
        val mid = (startIndex + endIndex) / 2
        when {
            list[mid] == value -> return mid
            list[mid] < value -> return binarySearch(list, value, mid + 1, endIndex)
            list[mid] > value -> return binarySearch(list, value, startIndex, mid - 1)
        }

    }
    return -1
}

fun <T : Comparable<T>> ArrayList<T>.binarySearch(value: T, range: IntRange = indices): Int? {
    if (range.first > range.last) {
        return null
    }

    val size = range.last - range.first + 1
    val middle = range.first + size / 2
    return when {
        this[middle] == value -> middle
        this[middle] > value -> binarySearch(value, range.first until middle)
        else -> binarySearch(value, (middle + 1)..range.last)
    }
}