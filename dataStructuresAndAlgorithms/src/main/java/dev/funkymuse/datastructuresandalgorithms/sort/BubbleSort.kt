package dev.funkymuse.datastructuresandalgorithms.sort

import dev.funkymuse.datastructuresandalgorithms.swap
import dev.funkymuse.datastructuresandalgorithms.swapAt

/**
Worst complexity: n^2
Average complexity: n^2
Best complexity: n
Space complexity: 1
Method: Exchanging
Stable: Yes
 */
fun <T : Comparable<T>> bubbleSort(list: MutableList<T>) {
    var swapped: Boolean
    /**
     * Sort the list in order until there are no swaps in the entire iteration.
     * If no swaps have been done—the list is ordered.
     */
    do {
        swapped = false
        for (i in 0 until list.size - 1) {
            if (list[i] > list[i + 1]) {
                list.swap(i, i + 1)
                swapped = true
            }
        }
    } while (swapped)
}


fun <T : Comparable<T>> MutableList<T>.bubbleSort(showPasses: Boolean = false) {
    if (size < 2) return
    for (end in (1 until size).reversed()) {
        var swapped = false
        for (current in 0 until end) {
            if (this[current] > this[current + 1]) {
                this.swapAt(current, current + 1)
                swapped = true
            }
        }
        if (showPasses) println(this)
        if (!swapped) return
    }
}