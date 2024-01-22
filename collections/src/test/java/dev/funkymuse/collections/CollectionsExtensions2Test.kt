package dev.funkymuse.collections

import org.junit.Assert.assertEquals
import org.junit.Test


class CollectionsExtensions2Test {
    @Test
    fun isListAndNullOrEmpty_behaviorCorrect() {
        val list = emptyList<Any>()

        var condition = false
        list.isListAndNullOrEmpty(actionFalse = { condition = false }, actionTrue = { condition = true })

        assertEquals(condition, true)
    }
}