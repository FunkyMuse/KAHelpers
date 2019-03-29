package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.mathematics


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

fun findFibonacciRecursive(n: Int, prev: Long = 1, current: Long = 1): Long{
    return if (n <= 2) current
    else findFibonacciRecursive(n - 1, current, prev + current)
}
