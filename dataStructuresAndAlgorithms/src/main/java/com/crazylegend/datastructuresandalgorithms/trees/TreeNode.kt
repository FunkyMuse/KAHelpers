package com.crazylegend.datastructuresandalgorithms.trees

import com.crazylegend.datastructuresandalgorithms.queue.ArrayListQueue
import com.crazylegend.datastructuresandalgorithms.queue.LinkedListQueue


typealias Visitor<T> = (TreeNode<T>) -> Unit


class TreeNode<T>(val value: T) {


    private val children: MutableList<TreeNode<T>> = mutableListOf()

    fun add(child: TreeNode<T>) {
        children.add(child)
    }


    fun forEachDepthFirst(visit: Visitor<T>) {
        visit(this)
        children.forEach {
            it.forEachDepthFirst(visit)
        }
    }

    fun find(value: T): TreeNode<T>? {
        var result: TreeNode<T>? = null
        forEachLevelOrder {
            if (it.value == value) {
                result = it
            }
        }
        return result
    }

    fun printEachLevel() {
        val queue = ArrayListQueue<TreeNode<T>>()

        var nodesLeftInTheCurrentLevel = 0

        queue.enqueue(this)

        while (queue.isNotEmpty) {
            nodesLeftInTheCurrentLevel = queue.count

            while (nodesLeftInTheCurrentLevel > 0) {
                val node = queue.dequeue() ?: break
                print("${node.value} ")
                node.children.forEach { queue.enqueue(it) }
                nodesLeftInTheCurrentLevel--
            }
            println()
        }
    }

    fun forEachLevelOrder(visit: Visitor<T>) {
        visit(this)
        val queue = LinkedListQueue<TreeNode<T>>()
        children.forEach { queue.enqueue(it) }

        var node = queue.dequeue()
        while (node != null) {
            visit(node)
            node.children.forEach { queue.enqueue(it) }
            node = queue.dequeue()
        }
    }
}