package com.crazylegend.datastructuresandalgorithms.linkedList

data class Node<T>(var value: T, var next: Node<T>? = null) {

    override fun toString(): String {
        return if (next != null) {
            "$value -> ${next.toString()}"
        } else {
            "$value"
        }
    }
}