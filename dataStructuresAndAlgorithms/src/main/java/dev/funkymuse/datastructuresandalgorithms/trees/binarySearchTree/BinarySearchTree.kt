package dev.funkymuse.datastructuresandalgorithms.trees.binarySearchTree

class BinarySearchTree<T : Comparable<T>> {

    var root: BinarySearchNode<T>? = null

    fun insert(value: T) {
        root = insert(root, value)
    }

    private fun insert(
            node: BinarySearchNode<T>?,
            value: T
    ): BinarySearchNode<T> {
        node ?: return BinarySearchNode(value)

        if (value < node.value) {
            node.leftChild = insert(node.leftChild, value)
        } else {
            node.rightChild = insert(node.rightChild, value)
        }

        return node
    }

    fun remove(value: T) {
        root = remove(root, value)
    }

    private fun remove(
            node: BinarySearchNode<T>?,
            value: T
    ): BinarySearchNode<T>? {
        node ?: return null

        when {
            value == node.value -> {

                if (node.leftChild == null && node.rightChild == null) {
                    return null
                }

                if (node.leftChild == null) {
                    return node.rightChild
                }

                if (node.rightChild == null) {
                    return node.leftChild
                }

                node.rightChild?.min?.value?.let {
                    node.value = it
                }

                node.rightChild = remove(node.rightChild, node.value)
            }
            value < node.value -> node.leftChild = remove(node.leftChild, value)
            else -> node.rightChild = remove(node.rightChild, value)
        }
        return node
    }

    override fun toString() = root?.toString() ?: "empty tree"

    fun contains(value: T): Boolean {

        var current = root


        while (current != null) {

            if (current.value == value) {
                return true
            }


            current = if (value < current.value) {
                current.leftChild
            } else {
                current.rightChild
            }
        }

        return false
    }

    override fun equals(other: Any?): Boolean {
        return other is BinarySearchTree<*> && this.root == other.root
    }

    fun contains(subtree: BinarySearchTree<T>): Boolean {
        val set = mutableSetOf<T>()
        root?.traverseInOrder {
            set.add(it)
        }

        var isEqual = true
        subtree.root?.traverseInOrder {
            isEqual = isEqual && set.contains(it)
        }
        return isEqual
    }

}
