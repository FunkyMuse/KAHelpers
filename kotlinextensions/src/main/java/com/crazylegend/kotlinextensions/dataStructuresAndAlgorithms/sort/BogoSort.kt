package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.sort


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


/**
 * Bogo sort is a very inefficient sorting algorithm.
 * Principle:
 *      Shuffle the list until it is sorted.
 *
Worst complexity: Infinite
Average complexity: n*n!
Best complexity: n
Space complexity: 1
Stable: No
 *
 */
fun <T: Comparable<T>> bogoSort(list: MutableList<T>) {
    /**
     * Returns true if the list is sorted.
     * Goes over every element and returns false if the order is not valid. In the end of the loop, in false has not
     * been returned, returns true.
     */
    fun isSorted(list: List<T>): Boolean {
        for (i in 1 until list.size) {
            if (list[i - 1] > list[i]) return false
        }
        return true
    }

    /**
     * Shuffle the list until it is sorted.
     */
    while (!isSorted(list)) {
        list.shuffle()
    }
}