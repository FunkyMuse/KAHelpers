package dev.funkymuse.datastructuresandalgorithms.sort




/**
 * Merge sort is an efficient algorithm for sorting lists of data.
 * Principle:
 *      Split the list in half until it is only 2 elements long, then sort those lists.
 *      After sorting those small lists, start merging them together.
 *      For instance, having 1 3 and 2 5, by merging we would get 1 2 3 5.
 *
 *
 * Time complexity: O(n log n) in all cases.
 *      Due to that, quicksort usually performs better on average due to its best case
 *      O(n) time complexity.
 *
 * Space complexity: O(n) auxiliary.
 */
fun <T : Comparable<T>> mergeSortPlain(list: MutableList<T>) = mergeSortPlain(list, 0, list.size - 1)

private fun <T : Comparable<T>> mergeSortPlain(list: MutableList<T>, start: Int, end: Int) {
    if (end > start) {
        val mid = (end + start) / 2
        mergeSortPlain(list, start, mid)
        mergeSortPlain(list, mid + 1, end)
        merge(list, start, mid, end)
    }
}

private fun <T : Comparable<T>> merge(list: MutableList<T>, start: Int, mid: Int, end: Int) {
    val numLeft = mid - start + 1
    val numRight = end - mid
    val leftList = ArrayList<T>(numLeft + 1)
    val rightList = ArrayList<T>(numRight + 1)
    for (i in 1..numLeft) {
        leftList.add(list[start + i - 1])
    }
    for (i in 1..numRight) {
        rightList.add(list[mid + i])
    }
    var i = 0
    var j = 0
    for (k in start..end) {
        list[k] = if (leftList[i] < rightList[j]) leftList[i++] else rightList[j++]
        if (i == numLeft) {
            for (p in (k + 1)..end) {
                list[p] = rightList[j++]
            }
            return
        }
        if (j == numRight) {
            for (p in (k + 1)..end) {
                list[p] = leftList[i++]
            }
            return
        }
    }
}