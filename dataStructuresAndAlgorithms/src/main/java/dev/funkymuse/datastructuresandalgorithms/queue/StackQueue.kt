package dev.funkymuse.datastructuresandalgorithms.queue


import dev.funkymuse.datastructuresandalgorithms.stack.Stack

/**
 * Same complexity as Ring buffer queue
 * @param T
 * @property leftStack Stack<T>
 * @property rightStack Stack<T>
 * @property count Int
 * @property isEmpty Boolean
 */
class StackQueue<T> : Queue<T> {
    private val leftStack = Stack<T>()
    private val rightStack = Stack<T>()

    override fun enqueue(element: T): Boolean = rightStack.push(element)

    override fun dequeue(): T? {
        if (leftStack.isEmpty)
            transferElements()

        return leftStack.pop()
    }

    override fun peek(): T? {
        if (leftStack.isEmpty) {
            transferElements()
        }
        return leftStack.peek()
    }

    override val count: Int
        get() = leftStack.count + rightStack.count
    override val isEmpty: Boolean
        get() = leftStack.isEmpty && rightStack.isEmpty


    private fun transferElements() {
        var nextElement = rightStack.pop()
        while (nextElement != null) {
            leftStack.push(nextElement)
            nextElement = rightStack.pop()
        }
    }

    override fun toString(): String = "Left stack: \n$leftStack \n Right stack:\n$rightStack"
}