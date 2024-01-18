package com.funkymuse.security

import org.junit.Test

class OneTimePasswordTest {


    @Test
    fun testRandomPasswordLength() {
        val length = 8

        val password = generateOneTimePassword(length)

        assert(password.length == length)
    }

    @Test
    fun exemptCharsTest() {
        val length = 8

        val exemptChars = listOf('a', 'b', 'c', 'd')
        val password = generateOneTimePassword(length)

        assert(password.length == length)

        exemptChars.forEach {
            assert(!password.contains(it))
        }
    }

}