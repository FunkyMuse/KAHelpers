package com.funkymuse.generators

import java.util.concurrent.atomic.AtomicInteger




object UIDGenerator {

    private const val START_UID = 0

    /**
     * https://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/
     */
    private val nextUID = AtomicInteger(START_UID)

    fun newUID(): Int {
        check(isValid(nextUID.get())) { "UID pool depleted" }
        return nextUID.incrementAndGet()
    }

    private fun isValid(uid: Int): Boolean = uid >= START_UID
}
