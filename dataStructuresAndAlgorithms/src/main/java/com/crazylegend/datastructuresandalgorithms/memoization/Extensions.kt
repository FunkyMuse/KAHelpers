package com.crazylegend.datastructuresandalgorithms.memoization

/**
 * Created by crazy on 1/4/21 to long live and prosper !
 */

fun <T, R> ((T) -> R).memoize(): (T) -> R = Memoize(this)
