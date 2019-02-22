package com.crazylegend.kotlinextensions.collections


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */



/**
 * USAGE
 * listOf(1, 2, 3) * 4 gives you [4, 8, 12]
 */
operator fun List<Int>.times(by: Int): List<Int> {
    return this.map { it * by }
}

/**
 * Swaps two values
 */
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}


fun <T> List<List<T>>.concat(): List<T> = fold(listOf()) { acc, l -> acc + l }
fun <T> List<List<T>>.concatToMutableList(): MutableList<T> = concat().toMutableList()