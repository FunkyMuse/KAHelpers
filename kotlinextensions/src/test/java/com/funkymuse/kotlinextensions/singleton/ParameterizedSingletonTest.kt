package com.funkymuse.kotlinextensions.singleton

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ParameterizedSingletonTest {

    private lateinit var createdObject: ParameterizedSingleton<String, Int>
    private val testInt = 15

    @Before
    fun initializeSingleton() {
        createdObject = ParameterizedSingleton {
            testInt.toString()
        }
    }

    @Test
    fun checkParameterizedSingletonValidity() {

        val instance = createdObject.getInstance(testInt)

        assertEquals(instance, testInt.toString())

    }


}