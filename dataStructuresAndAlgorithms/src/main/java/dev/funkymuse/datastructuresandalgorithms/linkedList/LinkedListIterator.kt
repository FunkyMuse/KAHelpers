package dev.funkymuse.datastructuresandalgorithms.linkedList

class LinkedListIterator<T>(private val list: LinkedList<T>) : Iterator<T>, MutableIterator<T> {

    private var lastNode: Node<T>? = null

    private var index = 0

    override fun hasNext() = index < list.size
    override fun next(): T {
        if (index >= list.size) throw IndexOutOfBoundsException()


        lastNode = if (index == 0) {
            //first iteration, there's no last node
            list.nodeAt(0)
        } else {
            lastNode?.next
        }

        index++
        return lastNode!!.value
    }

    override fun remove() {
        if (index == 1) {
            list.pop()
        } else {
            val previousNode = list.nodeAt(index - 2) ?: return
            list.removeAfter(previousNode)
            lastNode = previousNode
        }
        index--
    }
}
