package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.cryptography


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

/**
 * Caesar cipher is a more advanced substitution cipher than Atbash cipher.
 * Caesar cipher shifts all the letters in the alphabet by a certain amount, and then substitutes the letters with
 * the ones on positions i + s, where i is the index of the original letter, and s is the shift number.
 * The shift number is found out by computing shift % alphabet.size, where shift is the desired shift by the user and alphabet.size
 * is the number of letters in the alphabet.
 */
fun caesarEncrypt(text: String, shift: Int): String{
    val alphabet = listOf(
        'a', 'b', 'c', 'd', 'e',
        'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o',
        'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y',
        'z')

    val moduledShift = shift % alphabet.size
    if (moduledShift == 0) return text

    var resultString = ""
    text.forEach { character ->
        resultString += if (!alphabet.contains(character.toLowerCase())) character
        else {
            val newIndex = (alphabet.indexOf(character.toLowerCase()) + moduledShift) % alphabet.size
            if (character.isUpperCase()) alphabet[newIndex].toUpperCase() else alphabet[newIndex]
        }
    }
    return resultString
}

fun caesarDecrypt(text: String, shift: Int): String{
    val alphabet = listOf(
        'a', 'b', 'c', 'd', 'e',
        'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o',
        'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y',
        'z')

    val moduledShift = shift % alphabet.size
    if (moduledShift == 0) return text

    var resultString = ""
    text.forEach { character ->
        if (!alphabet.contains(character.toLowerCase())) resultString += character
        else {
            var newIndex = alphabet.indexOf(character.toLowerCase()) - moduledShift
            if (newIndex < 0) newIndex += alphabet.size
            resultString += if (character.isUpperCase()) alphabet[newIndex].toUpperCase() else alphabet[newIndex]
        }
    }
    return resultString
}