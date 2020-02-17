package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.problems


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */

fun generateWordsFixedLength(length: Int, alphabet: List<Char>): List<String> {
    var result = ArrayList<String>()
    for (i in 1..length) {
        val current = ArrayList<String>()
        if (result.isEmpty()) {
            for (char in alphabet) result.add("$char")
            continue
        }

        for (string in result) for (char in alphabet) current.add("$string$char")
        result = current
    }
    return result
}

/**
 * Potentially unoptimised.
 */
fun generateWordsUpperBoundLength(length: Int, alphabet: List<Char>): List<String> {
    val result = ArrayList<String>()
    for (i in 1..length) {
        val current = ArrayList<String>()
        if (result.isEmpty()) {
            for (char in alphabet) result.add("$char")
            continue
        }

        for (string in result) for (char in alphabet) {
            "$string$char".run {
                if (this.length == i) current.add(this)
            }
        }

        result.addAll(current)
    }
    return result
}