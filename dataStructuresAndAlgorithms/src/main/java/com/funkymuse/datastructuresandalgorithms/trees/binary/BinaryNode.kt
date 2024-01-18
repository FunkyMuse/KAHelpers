package com.funkymuse.datastructuresandalgorithms.trees.binary

import kotlin.math.max

typealias Visitor<T> = (T) -> Unit

class BinaryNode<T>(val value: T) {
    var leftChild: BinaryNode<T>? = null
    var rightChild: BinaryNode<T>? = null

    fun traverseInOrder(visit: Visitor<T>) {
        leftChild?.traverseInOrder(visit)
        visit(value)
        rightChild?.traverseInOrder(visit)
    }

    fun traversePreOrder(visit: Visitor<T>) {
        visit(value)
        leftChild?.traversePreOrder(visit)
        rightChild?.traversePreOrder(visit)
    }

    fun traversePostOrder(visit: Visitor<T>) {
        leftChild?.traversePostOrder(visit)
        rightChild?.traversePostOrder(visit)
        visit(value)
    }

    fun height(node: BinaryNode<T>? = this): Int {
        return node?.let { 1 + max(height(node.leftChild), height(node.rightChild)) } ?: -1
    }

    fun serialize(node: BinaryNode<T> = this): MutableList<T?> {
        val list = mutableListOf<T?>()
        node.traversePreOrderWithNull { list.add(it) }
        return list
    }

    fun deserialize(list: MutableList<T?>): BinaryNode<T?>? {
        val rootValue = list.removeAt(list.size - 1) ?: return null

        val root = BinaryNode<T?>(rootValue)

        root.leftChild = deserialize(list)
        root.rightChild = deserialize(list)

        return root
    }

    fun deserializeOptimized(list: MutableList<T?>): BinaryNode<T?>? {
        return deserialize(list.asReversed())
    }

    fun traversePreOrderWithNull(visit: (T?) -> Unit) {
        visit(value)
        leftChild?.traversePreOrderWithNull(visit) ?: visit(null)
        rightChild?.traversePreOrderWithNull(visit) ?: visit(null)
    }

    override fun toString() = diagram(this)

    /**
     * This algorithm is based on an implementation by Károly Lőrentey in his
    book Optimizing Collections, available from https://www.objc.io/books/optimizing-collections/
     * @param node BinaryNode<T>?
     * @param top String
     * @param root String
     * @param bottom String
     * @return String
     */
    private fun diagram(node: BinaryNode<T>?,
                        top: String = "",
                        root: String = "",
                        bottom: String = ""): String {
        return node?.let {
            if (node.leftChild == null && node.rightChild == null) {
                "$root${node.value}\n"
            } else {
                diagram(node.rightChild, "$top ", "$top┌──", "$top│ ") +
                        root + "${node.value}\n" + diagram(node.leftChild,
                        "$bottom│ ", "$bottom└──", "$bottom ")
            }
        } ?: "${root}null\n"
    }
}