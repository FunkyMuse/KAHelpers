package com.funkymuse.security

import kotlin.random.Random




fun charSubstituteEncrypt(text: String, key: String): String {
    var newKey = key
    var resultString = ""
    if (key.length > text.length) newKey = key.substring(0, text.length)
    if (key.isEmpty()) return text
    else if (key.length != text.length) {
        newKey = ""
        var current = 0
        while (newKey.length != text.length) {
            newKey += (if (key.length == 1) key[0] else key[current % (key.length)])
            current++
        }
    }

    text.forEachIndexed { index, character ->
        val charInt = character.toInt()
        val keyCharInt = newKey[index].toInt()
        val newCharInt = charInt + keyCharInt
        resultString += newCharInt.toChar()
    }

    return resultString
}

fun charSubstituteDecrypt(text: String, key: String): String {
    var newKey = key
    var resultString = ""
    if (key.length > text.length) newKey = key.substring(0, text.length)
    if (key.isEmpty()) return text
    else if (key.length != text.length) {
        newKey = ""
        var current = 0
        while (newKey.length != text.length) {
            newKey += (if (key.length == 1) key[0] else key[current % (key.length)])
            current++
        }
    }

    text.forEachIndexed { index, character ->
        val charInt = character.toInt()
        val keyCharInt = newKey[index].toInt()
        resultString += (charInt - keyCharInt).toChar()
    }

    return resultString
}

fun charSubstituteSecurelyEncrypt(text: String, keyLength: Int = 2): Pair<String, String> {
    if (keyLength < 2 || keyLength > (text.length / 2)) return Pair(text, "INSECURE KEY LENGTH")
    val fixedKeyLength = when {
        keyLength > text.length -> text.length
        keyLength < 2 -> 2
        else -> keyLength
    }

    val parts = text.chunked(fixedKeyLength)
    var result = ""
    var key = ""
    for (part in parts) {
        var randomKey = ""
        while (randomKey.length != part.length) randomKey += Random.nextInt(33, 126).toChar()
        key += randomKey
        result += charSubstituteEncrypt(part, randomKey)
    }
    return Pair(result, key)
}