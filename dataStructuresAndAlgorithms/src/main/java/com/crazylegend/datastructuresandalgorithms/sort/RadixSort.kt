package com.crazylegend.datastructuresandalgorithms.sort

import kotlin.math.pow

fun MutableList<Int>.radixSort() {
    val base = 10
    var done = false
    var digits = 1
    while (!done) {
        done = true
        val buckets = arrayListOf<MutableList<Int>>().apply {
            for (i in 0..9) {
                this.add(arrayListOf())
            }
        }
        this.forEach { number ->
            val remainingPart = number / digits
            val digit = remainingPart % base
            buckets[digit].add(number)

            if (remainingPart > 0) {
                done = false
            }
        }
        digits *= base
        this.clear()
        this.addAll(buckets.flatten())
    }
}

fun Int.digits(): Int {
    var count = 0
    var num = this
    while (num != 0) {
        count += 1
        num /= 10
    }
    return count
}

fun Int.digit(atPosition: Int): Int? {
    if (atPosition > digits()) return null
    var num = this
    val correctedPosition = (atPosition + 1).toDouble()
    while (num / (10.0.pow(correctedPosition).toInt()) != 0) {
        num /= 10
    }
    return num % 10
}


fun MutableList<Int>.lexicographicalSort() {
    val newList = msdRadixSorted(this, 0)
    this.clear()
    this.addAll(newList)
}

private fun msdRadixSorted(list: MutableList<Int>, position: Int): MutableList<Int> {
    if (position >= list.maxDigits()) return list

    val buckets = arrayListOf<MutableList<Int>>().apply {
        for (i in 0..9) {
            this.add(arrayListOf())
        }
    }
    val priorityBucket = arrayListOf<Int>()
    list.forEach { number ->
        val digit = number.digit(position)
        if (digit == null) {
            priorityBucket.add(number)
            return@forEach
        }
        buckets[digit].add(number)
    }

    val newValues = buckets.reduce { result, bucket ->
        if (bucket.isEmpty()) return@reduce result
        result.addAll(msdRadixSorted(bucket, position + 1))
        result
    }
    priorityBucket.addAll(newValues)

    return priorityBucket
}

private fun List<Int>.maxDigits(): Int {
    return this.max()?.digits() ?: 0
}