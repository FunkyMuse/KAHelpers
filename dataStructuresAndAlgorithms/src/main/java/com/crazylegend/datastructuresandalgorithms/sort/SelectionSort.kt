package com.crazylegend.datastructuresandalgorithms.sort


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


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