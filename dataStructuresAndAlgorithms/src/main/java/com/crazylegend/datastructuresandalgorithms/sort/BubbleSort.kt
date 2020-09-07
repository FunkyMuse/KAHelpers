package com.crazylegend.datastructuresandalgorithms.sort

import com.crazylegend.datastructuresandalgorithms.swap


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


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