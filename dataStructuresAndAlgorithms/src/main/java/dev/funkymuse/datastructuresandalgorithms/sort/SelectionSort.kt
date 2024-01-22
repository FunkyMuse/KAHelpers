package dev.funkymuse.datastructuresandalgorithms.sort

import dev.funkymuse.datastructuresandalgorithms.swapAt

/**
 * Selection sort is an inefficient* primitive sorting algorithm.
 * Principle:
 *      Go through the list and find the smallest element in the list. After it was found,
 *      swap the element with the element on Nth place in the list, where N is the number of iterations that
 *      was already done.
 *
 * Time complexity: O(n^2) in all cases.
 * Space complexity: O(1) for the original list.
 */
fun <T : Comparable<T>> selectionSort(list: MutableList<T>) {
    for (i in 0 until list.size) {
        var minIndex = i
        for (j in i + 1 until list.size) {
            if (list[j] < list[minIndex]) minIndex = j
        }
        if (minIndex != i) {
            val tmp = list[i]
            list[i] = list[minIndex]
            list[minIndex] = tmp
        }
    }
}

fun <T : Comparable<T>> MutableList<T>.selectionSort(showPasses: Boolean = false) {

    if (size < 2) return

    for (current in 0 until (size - 1)) {
        var lowest = current
        for (other in (current + 1) until size) {
            if (this[lowest] > this[other]) {
                lowest = other
            }
        }
        if (lowest != current) {
            this.swapAt(lowest, current)
        }
        if (showPasses) println(this)
    }
}