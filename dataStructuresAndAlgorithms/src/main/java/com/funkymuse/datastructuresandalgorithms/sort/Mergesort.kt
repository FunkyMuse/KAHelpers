package com.funkymuse.datastructuresandalgorithms.sort



fun <T : Comparable<T>> List<T>.mergeSort(): List<T> {
    if (this.size < 2) return this
    val middle = this.size / 2
    val left = this.subList(0, middle).mergeSort()
    val right = this.subList(middle, this.size).mergeSort()

    return merge(left, right)
}

private fun <T : Comparable<T>> merge(left: List<T>, right: List<T>): List<T> {
    var leftIndex = 0
    var rightIndex = 0
    val result = mutableListOf<T>()
    while (leftIndex < left.size && rightIndex < right.size) {
        val leftElement = left[leftIndex]
        val rightElement = right[rightIndex]
        when {
            leftElement < rightElement -> {
                result.add(leftElement)
                leftIndex += 1
            }
            leftElement > rightElement -> {
                result.add(rightElement)
                rightIndex += 1
            }
            else -> {
                result.add(leftElement)
                leftIndex += 1
                result.add(rightElement)
                rightIndex += 1
            }
        }
    }
    if (leftIndex < left.size) {
        result.addAll(left.subList(leftIndex, left.size))
    }
    if (rightIndex < right.size) {
        result.addAll(right.subList(rightIndex, right.size))
    }
    return result
}

fun <T : Comparable<T>> merge(
        first: Iterable<T>,
        second: Iterable<T>
): Iterable<T> {

    val result = mutableListOf<T>()
    val firstIterator = first.iterator()
    val secondIterator = second.iterator()

    if (!firstIterator.hasNext()) return second
    if (!secondIterator.hasNext()) return first

    var firstEl = firstIterator.nextOrNull()
    var secondEl = secondIterator.nextOrNull()

    while (firstEl != null && secondEl != null) {
        when {
            firstEl < secondEl -> {
                result.add(firstEl)
                firstEl = firstIterator.nextOrNull()
            }
            secondEl < firstEl -> {
                result.add(secondEl)
                secondEl = secondIterator.nextOrNull()
            }
            else -> {
                result.add(firstEl)
                result.add(secondEl)
                firstEl = firstIterator.nextOrNull()
                secondEl = secondIterator.nextOrNull()
            }
        }
    }

    while (firstEl != null) {
        result.add(firstEl)
        firstEl = firstIterator.nextOrNull()
    }
    while (secondEl != null) {
        result.add(secondEl)
        secondEl = secondIterator.nextOrNull()
    }

    return result
}

private fun <T> Iterator<T>.nextOrNull(): T? {
    return if (this.hasNext()) this.next() else null
}