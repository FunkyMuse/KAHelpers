package com.funkymuse.datastructuresandalgorithms.sort

/**
 * Merge Sort using Sentinels - Int.MAX_VALUE entries when merging lists.
 * Allows to eliminate checks for whether the left/right list are fully added to the original list.
 * Performs better than plain merge sort at the cost of extra O(2 (log N)) auxiliary memory.
 *
 * Time complexity per case:
 *      Best, Average, Worst - O(n log n), logarithmic.
 *
 * Space complexity:
 *      Auxiliary O(n) memory.
 */


/**
 * To sort a list, this method calls mergeSortSentinel with start = 0 and end = the index of the last element in the list.
 */
fun mergeSortSentinel(list: MutableList<Int>) = mergeSortSentinel(list, 0, list.size - 1)

private fun mergeSortSentinel(list: MutableList<Int>, start: Int, end: Int) {
    /**
     * While the list has more than 1 element, split it in two and recursively sort those two lists.
     * After they have been sorted, merge them together.
     */
    if (end > start) {
        val mid = (end + start) / 2
        mergeSortSentinel(list, start, mid)
        mergeSortSentinel(list, mid + 1, end)
        merge(list, start, mid, end)
    }
}

/**
 * Merging two sorted lists takes O(n) time, which is the reason behind merge sort's efficiency.
 * Merging is separated in two parts:
 *      1. Putting all elements from both sublists into new lists, called left and right respectively.
 *      2. Putting the smallest remaining element from both the lists into the original list to be sorted at index k,
 *      where k is equal to start + number of elements put so far into the original list.
 */
private fun merge(list: MutableList<Int>, start: Int, mid: Int, end: Int) {

    val numLeft = mid - start + 1
    val numRight = end - mid
    val leftArray = IntArray(numLeft + 1)
    val rightArray = IntArray(numRight + 1)
    // Lines 50-55 put the elements into the new sublists.
    for (i in 1..numLeft) {
        leftArray[i - 1] = list[start + i - 1]
    }
    for (i in 1..numRight) {
        rightArray[i - 1] = list[mid + i]
    }
    /*  Lines 57-58 put two special values—called sentinels—into the lists. This is done to further improve
        the performance on lines 64-66, when actual merging happens.
    */
    leftArray[numLeft] = Int.MAX_VALUE
    rightArray[numRight] = Int.MAX_VALUE
    var i = 0
    var j = 0
    for (k in start..end) {
        list[k] = if (leftArray[i] < rightArray[j]) leftArray[i++] else rightArray[j++]
    }
}
