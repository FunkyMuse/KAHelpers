package com.funkymuse.datastructuresandalgorithms.trees.binarySearchTree

typealias Visitor<T> = (T) -> Unit

class BinarySearchNode<T : Comparable<T>>(var value: T) {

    var leftChild: BinarySearchNode<T>? = null
    var rightChild: BinarySearchNode<T>? = null

    val min: BinarySearchNode<T>?
        get() = leftChild?.min ?: this

    val isBinarySearchTree: Boolean
        get() = isBST(this, min = null, max = null)

    override fun toString() = diagram(this)

    private fun diagram(node: BinarySearchNode<T>?,
                        top: String = "",
                        root: String = "",
                        bottom: String = ""): String {
        return node?.let {
            if (node.leftChild == null && node.rightChild == null) {
                "$root${node.value}\n"
            } else {
                diagram(node.rightChild, "$top ", "$top┌──", "$top│ ") +
                        root + "${node.value}\n" + diagram(node.leftChild, "$bottom│ ", "$bottom└──", "$bottom ")
            }
        } ?: "${root}null\n"
    }

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

    private fun isBST(tree: BinarySearchNode<T>?, min: T?, max: T?): Boolean {
        tree ?: return true

        if (min != null && tree.value <= min) {
            return false
        } else if (max != null && tree.value > max) {
            return false
        }
        return isBST(tree.leftChild, min, tree.value) && isBST(tree.rightChild, tree.value, max)
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is BinarySearchNode<*>) {
            this.value == other.value &&
                    this.leftChild == other.leftChild &&
                    this.rightChild == other.rightChild
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + (leftChild?.hashCode() ?: 0)
        result = 31 * result + (rightChild?.hashCode() ?: 0)
        return result
    }

}
