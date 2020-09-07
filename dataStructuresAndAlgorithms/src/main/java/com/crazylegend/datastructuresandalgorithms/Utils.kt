package com.crazylegend.datastructuresandalgorithms

import com.crazylegend.datastructuresandalgorithms.sort.selectionSort
import java.util.*

/**
 * Created by crazy on 9/7/20 to long live and prosper !
 */


internal fun <T> List<T>.swap(i: Int, j: Int): List<T> {
    if (isInBounds(i) && isInBounds(j)) {
        Collections.swap(this, i, j)
    }
    return this
}

private fun <T> List<T>.isInBounds(index: Int): Boolean {
    return index in 0..lastIndex
}

fun <T> MutableList<T>.swapAt(first: Int, second: Int) {
    val aux = this[first]
    this[first] = this[second]
    this[second] = aux
}

/**
 * Given a list of Comparable elements, bring all instances of a given value in the list to
the right side of the array.
 * @receiver MutableList<T>
 * @param element T
 */
fun <T : Comparable<T>> MutableList<T>.rightAlign(element: T) {
    if (size < 2) return
    var searchIndex = size - 2
    while (searchIndex >= 0) {
        if (this[searchIndex] == element) {
            var moveIndex = searchIndex
            while (moveIndex < size - 1 &&
                    this[moveIndex + 1] != element) {
                swapAt(moveIndex, moveIndex + 1)
                moveIndex++
            }
        }
        searchIndex--
    }
}

fun <T : Comparable<T>> MutableList<T>.biggestDuplicate(): T? {
    selectionSort()
    for (i in (1 until this.size).reversed()) {
        if (this[i] == this[i - 1]) {
            return this[i]
        }
    }
    return null
}

/**
 * The time complexity of this solution is O(n).
 * @receiver MutableList<T>
 */
fun <T : Comparable<T>> MutableList<T>.rev() {
    var left = 0
    var right = this.size - 1
    while (left < right) {
        swapAt(left, right)
        left++
        right--
    }
}


fun <T> Array<T>.swapAt(first: Int, second: Int) {
    val aux = this[first]
    this[first] = this[second]
    this[second] = aux
}

val ascending = Comparator { first: Int, second: Int ->
    when {
        first < second -> -1
        first > second -> 1
        else -> 0
    }
}

val descending = Comparator { first: Int, second: Int ->
    when {
        first < second -> 1
        first > second -> -1
        else -> 0
    }
}