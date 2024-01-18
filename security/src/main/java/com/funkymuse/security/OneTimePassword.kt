package com.funkymuse.security

import java.security.SecureRandom


fun generateOneTimePassword(
    passwordLength: Int = 8, shuffleCharacters: Boolean = true,
    random: SecureRandom = SecureRandom(), exemptChars: List<Char> = emptyList()
): String {
    val chars = ("abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789!@%$%&^?|~'\"#+="
            + "\\*/.,:;[]()-_<>").toCharArray()

    if (shuffleCharacters) {
        chars.shuffle()
    }

    val finalChars = chars.toMutableList().also {
        it.removeAll(exemptChars)
    }
    val passChars = String(finalChars.toCharArray())
    val pass = StringBuilder()
    for (i in 0 until passwordLength) pass.append(chars[random.nextInt(passChars.length)])
    return pass.toString()
}

fun generateOneTimePassword(
    chars: CharArray = ("abcdefghijklmnopqrstuvwxyz"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789!@%$%&^?|~'\"#+="
            + "\\*/.,:;[]()-_<>").toCharArray(),
    passwordLength: Int = 8,
    shuffleCharacters: Boolean = true,
    random: SecureRandom = SecureRandom(),
    exemptChars: List<Char> = emptyList()
): String {

    if (shuffleCharacters) {
        chars.shuffle()
    }

    val finalChars = chars.toMutableList().also {
        it.removeAll(exemptChars)
    }
    val passChars = String(finalChars.toCharArray())
    val pass = StringBuilder()
    for (i in 0 until passwordLength) pass.append(chars[random.nextInt(passChars.length)])
    return pass.toString()
}

