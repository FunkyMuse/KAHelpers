package com.crazylegend.datastructuresandalgorithms.mathematics


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
 * The method computes the greatest common divisor of two numbers by recursively applying the Euclidean algorithm:
 *      gcd(a, 0) = a
 *      gcd(a, b) = gcd(b, a mod b)
 */
fun findGreatestCommonDivisor(first: Long, second: Long): Long {
    val smaller = if (first <= second) first else second
    val bigger = if (first > second) first else second

    return if (bigger % smaller == 0L) smaller
    else findGreatestCommonDivisor(smaller, bigger % smaller)
}