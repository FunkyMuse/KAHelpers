package com.crazylegend.datastructuresandalgorithms

import java.util.*

/**
 * Created by crazy on 9/7/20 to long live and prosper !
 */


internal fun <T> List<T>.swap(i: Int, j: Int): List<T> {
    if (isInBounds(i) && isInBounds(j)) {
        Collections.swap(this, i, j)
    }
    return this
}

private fun <T> List<T>.isInBounds(index: Int): Boolean {
    return index in 0..lastIndex
}