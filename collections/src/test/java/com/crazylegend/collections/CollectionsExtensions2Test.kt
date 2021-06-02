package com.crazylegend.collections

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by crazy on 6/15/20 to long live and prosper !
 */
class CollectionsExtensions2Test {
    @Test
    fun isListAndNullOrEmpty_behaviorCorrect() {
        val list = emptyList<Any>()

        var condition = false
        list.isListAndNullOrEmpty(actionFalse = { condition = false }, actionTrue = { condition = true })

        assertEquals(condition, true)
    }
}