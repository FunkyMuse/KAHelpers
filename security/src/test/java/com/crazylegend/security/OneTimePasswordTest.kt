package com.crazylegend.security

import org.junit.Test

/**
 * Created by crazy on 1/4/21 to long live and prosper !
 */
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