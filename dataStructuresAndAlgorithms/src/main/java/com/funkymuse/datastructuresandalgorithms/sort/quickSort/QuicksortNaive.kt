package com.funkymuse.datastructuresandalgorithms.sort.quickSort

/**
 * The naive partitioning creates a new list on every filter function; this is inefficient.
 * @receiver List<T>
 * @return List<T>
 */
fun <T : Comparable<T>> List<T>.quicksortNaive(): List<T> {
    if (this.size < 2) return this

    val pivot = this[this.size / 2]
    val less = this.filter { it < pivot }
    val equal = this.filter { it == pivot }
    val greater = this.filter { it > pivot }
    return less.quicksortNaive() + equal + greater.quicksortNaive()
}