package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.mathematics


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
 * The least common multiple is found out by using the principle that
 * lcm(a, b) = (a * b) / gcd(a, b)
 *
 * And using an efficient algorithm called the Euclidean algorithm to compute the greatest common divisor.
 */
fun findLeastCommonMultiple(a: Int, b: Int): Int {
    fun findGreatestCommonDivisor(first: Long, second: Long): Long {
        val smaller = if (first <= second) first else second
        val bigger = if (first > second) first else second

        return if (bigger % smaller == 0L) smaller
        else findGreatestCommonDivisor(smaller, bigger % smaller)
    }

    return ((a * b) / findGreatestCommonDivisor(a.toLong(), b.toLong())).toInt()
}