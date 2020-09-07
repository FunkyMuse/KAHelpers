package com.crazylegend.datastructuresandalgorithms.ringBuffer

class RingBuffer<T>(private val size: Int) {

    private var array = ArrayList<T?>(size)
    private var readIndex = 0
    private var writeIndex = 0

    val count: Int
        get() = availableSpaceForReading

    private val availableSpaceForReading: Int
        get() = (writeIndex - readIndex)

    val first: T?
        get() = array.getOrNull(readIndex)

    val isEmpty: Boolean
        get() = (count == 0)

    private val availableSpaceForWriting: Int
        get() = (size - availableSpaceForReading)

    val isFull: Boolean
        get() = (availableSpaceForWriting == 0)

    fun write(element: T): Boolean {
        return if (!isFull) {
            if (array.size < size) {
                array.add(element)
            } else {
                array[writeIndex % size] = element
            }
            writeIndex += 1
            true
        } else {
            false
        }
    }

    fun read(): T? {
        return if (!isEmpty) {
            val element = array[readIndex % size]
            readIndex += 1
            element
        } else {
            null
        }
    }

    override fun toString(): String {
        val values = (0 until availableSpaceForReading).map { offset ->
            "${array[(readIndex + offset) % size]!!}"
        }
        return values.joinToString(prefix = "[", separator = ", ", postfix = "]")
    }

}
