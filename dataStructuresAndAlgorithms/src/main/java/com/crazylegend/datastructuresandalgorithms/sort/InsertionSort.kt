package com.crazylegend.datastructuresandalgorithms.sort

import com.crazylegend.datastructuresandalgorithms.swapAt


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


/**
 *
Worst complexity: n^2
Average complexity: n^2
Best complexity: n
Space complexity: 1
Method: Insertion
Stable: Yes
 *
 */
fun <T : Comparable<T>> insertionSort(list: MutableList<T>) {
    for (j in 1 until list.size) {
        val key = list[j]
        var i = j - 1
        while (i >= 0 && list[i] > key) {
            list[i + 1] = list[i]
            i -= 1
        }
        list[i + 1] = key
    }
}

fun <T : Comparable<T>> MutableList<T>.insertionSort(showPasses: Boolean = false) {
    if (size < 2) return
    for (current in 1 until this.size) {
        for (shifting in (1..current).reversed()) {
            if (this[shifting] < this[shifting - 1]) {
                this.swapAt(shifting, shifting - 1)
            } else {
                break
            }
        }
        if (showPasses) println(this)
    }
}