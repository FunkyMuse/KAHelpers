package com.crazylegend.datastructuresandalgorithms.sort

import com.crazylegend.datastructuresandalgorithms.swap


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
Inventor: Tony Hoare
Worst complexity: n^2
Average complexity: n*log(n)
Best complexity: n*log(n)
Method: Partitioning
Stable: No
 */

fun <T : Comparable<T>> quickSort(list: MutableList<T>, low: Int = 0, high: Int = list.size - 1) {
    fun partition(list: MutableList<T>, low: Int, high: Int): Int {
        val pivot = list[high]
        var i = low - 1
        for (j in low until high) {
            if (list[j] < pivot && i != j) {
                i++
                list.swap(i, j)
            }
        }
        i++
        list.swap(i, high)
        return i
    }

    if (low < high) {
        val mid = partition(list, low, high)
        quickSort(list, low, mid - 1)
        quickSort(list, mid + 1, high)
    }

}
