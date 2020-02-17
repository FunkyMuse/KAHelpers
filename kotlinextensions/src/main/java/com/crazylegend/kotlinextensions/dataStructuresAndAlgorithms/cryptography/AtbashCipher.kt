package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.cryptography


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
 * Atbash cipher is one of the oldest ciphers known. It is a substitution cipher which works by reversing the alphabet.
 * For instance, "AZ" would become "ZA" and so on.
 *
 * The encryption method creates a reversed list from the original latin alphabet. The letters are then substituted
 * by finding out their index in the original list and replacing the letter with the one with the same index from
 * the reversed list.
 *
 * Decrypting works the same way, just with the lists in a different order.
 */

fun atbashEncrypt(text: String): String {
    // Create a list of all the letters in the English alphabet.
    val alphabet = listOf(
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y',
            'z')
    // Reverse it and put it in a new, reversed list.
    val reversedAlphabet = alphabet.reversed()

    // The string for output
    var resultString = ""
    /*
        For every character of the original text:
            If the character is not in the English alphabet, add it to the result.
            Otherwise (if the character is in the English alphabet), substitute it with a character on the same position
                in the reversed list, and then append it to the result.
     */
    text.forEach { character ->
        if (!alphabet.contains(character.toLowerCase())) resultString += character
        else {
            var newChar = reversedAlphabet[alphabet.indexOf(character.toLowerCase())]
            // If the original character is in upper case, the substitute should be upper case, too.
            if (character.isUpperCase()) newChar = newChar.toUpperCase()
            resultString += newChar
        }
    }
    return resultString
}

/**
 * For documentation refer above, as it works following exactly the same principle.
 */
fun atbashDecrypt(text: String): String {
    val alphabet = listOf(
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y',
            'z')
    val reversedAlphabet = alphabet.reversed()

    var resultString = ""
    text.forEach { character ->
        if (!alphabet.contains(character.toLowerCase())) resultString += character
        else {
            var newChar = alphabet[reversedAlphabet.indexOf(character.toLowerCase())]
            if (character.isUpperCase()) newChar = newChar.toUpperCase()
            resultString += newChar
        }
    }
    return resultString
}