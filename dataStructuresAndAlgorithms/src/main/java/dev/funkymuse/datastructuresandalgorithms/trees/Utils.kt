package dev.funkymuse.datastructuresandalgorithms.trees

import kotlin.math.pow

fun leafNodes(height: Int): Int {
    return 2.0.pow(height).toInt()
}

fun nodes(height: Int): Int {
    return 2.0.pow(height + 1).toInt() - 1
}