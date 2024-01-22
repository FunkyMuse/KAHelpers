package dev.funkymuse.security

import kotlin.random.Random




fun vigenereEncrypt(text: String, key: String): String {
    val alphabet = listOf(
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y',
            'z'
    )
    var newKey = ""
    var resultString = ""
    if (key.length > text.length) newKey = key.substring(0, text.length)
    if (key.isEmpty()) return text
    else {
        var current = 0
        while (newKey.length != text.length) {
            newKey += (if (key.length == 1) key[0] else key[current % (key.length)])
            current++
        }
    }
    newKey = newKey.lowercase()

    text.forEachIndexed { index, character ->
        resultString += if (alphabet.contains(character.lowercaseChar())) {
            val newIndex =
                    (alphabet.indexOf(character.lowercaseChar()) + alphabet.indexOf(newKey[index])) % alphabet.size
            val newCharacter = if (character.isUpperCase()) alphabet[newIndex].uppercase() else alphabet[newIndex]
            newCharacter
        } else {
            character
        }
    }
    return resultString
}

fun vigenereDecrypt(text: String, key: String): String {
    val alphabet = listOf(
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y',
            'z'
    )
    var newKey = ""
    var resultString = ""
    if (key.length > text.length) newKey = key.substring(0, text.length)
    if (key.isEmpty()) return text
    else {
        var current = 0
        while (newKey.length != text.length) {
            newKey += (if (key.length == 1) key[0] else key[current % (key.length)])
            current++
        }
    }
    newKey = newKey.lowercase()

    text.forEachIndexed { index, character ->
        if (alphabet.contains(character.lowercaseChar())) {
            var newIndex = (alphabet.indexOf(character.lowercaseChar()) - alphabet.indexOf(newKey[index]))
            if (newIndex < 0) newIndex += alphabet.size
            val newCharacter = if (character.isUpperCase()) alphabet[newIndex].uppercase() else alphabet[newIndex]
            resultString += newCharacter
        } else {
            resultString += character
        }
    }
    return resultString
}

/**
 * Encrypts using randomly generated keys for every N characters of the input string.
 * Outputs a pair where the first element is the encrypted output and the second element is the random key.
 *
 * This imitates the so-called One Time Pad, which was proven to be cryptographically unbreakable given its proper usage.
 */
fun vigenereSecurelyEncrypt(text: String, keyLength: Int = 2): Pair<String, String> {
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
        while (randomKey.length != part.length) randomKey += Random.nextInt(97, 122).toChar()
        key += randomKey
        result += vigenereEncrypt(part, randomKey)
    }
    return Pair(result, key)
}