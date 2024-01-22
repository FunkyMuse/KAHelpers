package dev.funkymuse.datastructuresandalgorithms.queue.priority

class CustomPriorityQueueArrayList<T : Comparable<T>> :
        AbstractPriorityQueueArrayList<T>() {

    override fun sort() {
        var index = count - 2
        while (index >= 0 &&
                elements[index + 1] > elements[index]) {
            swap(index, index + 1)
            index--;
        }
    }

    private fun swap(i: Int, j: Int) {
        val tmp = elements[i]
        elements[i] = elements[j]
        elements[j] = tmp
    }

    override val isEmpty: Boolean
        get() = elements.isEmpty()
}