package dev.funkymuse.datastructuresandalgorithms.sort.quickSort

import java.util.Stack

fun <T : Comparable<T>> MutableList<T>.quicksortIterativeLomuto(low: Int, high: Int) {
    val stack = Stack<Int>()
    stack.push(low)
    stack.push(high)

    while (stack.isNotEmpty()) {
        val end = stack.pop() ?: continue
        val start = stack.pop() ?: continue
        val p = this.partitionLomuto(start, end)
        if ((p - 1) > start) {
            stack.push(start)
            stack.push(p - 1)
        }
        if ((p + 1) < end) {
            stack.push(p + 1)
            stack.push(end)
        }
    }
}