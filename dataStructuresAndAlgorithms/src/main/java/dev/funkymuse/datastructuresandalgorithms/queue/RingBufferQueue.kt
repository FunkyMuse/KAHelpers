package dev.funkymuse.datastructuresandalgorithms.queue


import dev.funkymuse.datastructuresandalgorithms.ringBuffer.RingBuffer

class RingBufferQueue<T>(size: Int) : Queue<T> {

    /**
     * Space complexity O(N) due to buffer's size implementation
     * The ring buffer has a fixed size, which means that enqueue can fail.
     */
    private val ringBuffer = RingBuffer<T>(size)

    /**
     * Adds element at the start of the queue O(1)
     * @return T?
     */
    override fun enqueue(element: T) = ringBuffer.write(element)

    /**
     * Removes the first element of the queue O(1)
     * @return T?
     */
    override fun dequeue(): T? = ringBuffer.read()

    /**
     * Returns the first element of the queue O(1)
     * @return T?
     */
    override fun peek(): T? = ringBuffer.first

    override val count: Int
        get() = ringBuffer.count

    override val isEmpty: Boolean
        get() = ringBuffer.isEmpty

    override fun toString(): String = ringBuffer.toString()
}