package dev.funkymuse.datastructuresandalgorithms.linkedList

import java.io.Serializable

class LinkedList<T> : Iterable<T>, Collection<T>, MutableIterable<T>, MutableCollection<T>, Serializable, Cloneable {

    override fun add(element: T): Boolean {
        append(element)
        return true
    }

    override fun addAll(elements: Collection<T>): Boolean {
        for (element in elements) {
            append(element)
        }
        return true
    }

    override fun clear() {
        head = null
        tail = null
        size = 0
    }

    override fun remove(element: T): Boolean {
        val iterator = iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item == element) {
                iterator.remove()
                return true
            }
        }
        return false
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        for (item in elements) {
            result = remove(item) || result
        }
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var result = false
        val iterator = iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (!elements.contains(item)) {
                iterator.remove()
                result = true
            }
        }
        return result
    }

    /**
     * Checks whether the element is in the list
     * Time complexity O(n)
     * @param element T
     * @return Boolean
     */
    override fun contains(element: T): Boolean {
        for (item in this) {
            if (item == element) return true
        }
        return false
    }

    /**
     * Checks whether the elements are present into the list
     * Time complexity O(n^2)
     * @param elements Collection<T>
     * @return Boolean
     */
    override fun containsAll(elements: Collection<T>): Boolean {
        for (searched in elements) {
            if (!contains(searched)) return false
        }
        return true
    }

    override fun isEmpty() = isListEmpty

    override fun iterator(): MutableIterator<T> = LinkedListIterator(this)

    private var head: Node<T>? = null
    private var tail: Node<T>? = null

    override var size = 0
        private set

    /**
     * Checks whether the list is empty
     */
    private val isListEmpty get() = size == 0

    /**
     * Checks whether the list is not empty
     */
    fun isNotEmpty() = !isListEmpty

    //region insertion
    /**
     * Adds an element to the first position in the list a.k.a the head
     * Time complexity O(1)
     * @param value T
     * @return LinkedList<T> so that you can chain more push calls
     */
    fun push(value: T): LinkedList<T> {
        head = Node(value = value, next = head)
        if (tail == null) {
            tail = head
        }
        incrementListSize()
        return this
    }

    /**
     * Adds an element to the end of the list a.k.a the tail
     * Time complexity O(1)
     * @param value T
     */
    fun append(value: T) {
        // if the list is empty, this is identical to a push and we have a non-null tail
        if (isListEmpty) {
            push(value)
            return
        }
        //since tail is never null because of the code above, we don't have any next value for our tail
        tail?.next = Node(value)
        //because this is tail-end insertion our new node is also our tail
        tail = tail?.next
        incrementListSize()
    }

    /**
     * Find a node at specific index
     * Time complexity O(index)
     * where index is the parameter
     * @param index Int
     * @return Node<T>?
     */
    fun nodeAt(index: Int): Node<T>? {
        var currentNode = head
        var currentIndex = 0

        while (currentNode != null && currentIndex < index) {
            currentNode = currentNode.next
            currentIndex++
        }
        return currentNode
    }


    /**
     * Insert a value after a node
     * Time complexity O(1)
     * @param value T
     * @param afterNode Node<T>
     * @return Node<T> the new node
     */
    fun insert(value: T, afterNode: Node<T>): Node<T> {
        if (tail == afterNode) {
            append(value)
            return tail!!
        }

        val newNode = Node(value, afterNode.next)
        afterNode.next = newNode
        incrementListSize()
        return newNode
    }
    //endregion


    //region deletion

    /**
     * Removes a value at the front of the list
     * @return T? the value that was removed
     */
    fun pop(): T? {
        if (isNotEmpty()) decrementListSize()

        val result = head?.value
        head = head?.next

        if (isListEmpty) {
            tail = null
        }
        return result
    }

    /**
     * Remove element after a node (remove the immediate next node).
     * Time complexity O(1)
     * @param node Node<T>
     * @return T? element's value
     */
    fun removeAfter(node: Node<T>): T? {
        val result = node.next?.value
        if (node.next == tail) {
            tail = node
        }
        if (node.next != null) {
            size--
        }

        node.next = node.next?.next
        return result
    }

    /**
     * Remove the last element of the list
     * Time complexity O(n)
     * @return T? the value of the removed element
     */
    fun removeLast(): T? {
        val head = head ?: return null

        //if list has only one element, so pop will function well here since it updates the head and tail
        if (head.next == null) return pop()

        decrementListSize()

        var previous = head
        var current = head

        var next = current.next
        while (next != null) {
            previous = current
            current = next
            next = current.next
        }

        previous.next = null
        tail = previous
        return current.value
    }
    //endregion


    private fun decrementListSize() {
        size--
    }

    private fun incrementListSize() {
        size++
    }

    override fun toString(): String {
        return if (isListEmpty) {
            "Empty list"
        } else {
            head.toString()
        }
    }
}