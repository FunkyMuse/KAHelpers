package com.funkymuse.datastructuresandalgorithms.trees.binarySearchTree

abstract class TraversableBinaryNode<Self : TraversableBinaryNode<Self, T>, T>(var value: T) {

    var leftChild: Self? = null
    var rightChild: Self? = null

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

}
