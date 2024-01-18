package com.funkymuse.datastructuresandalgorithms.problems

import java.util.Stack

fun isStringBalanced(s: String): Boolean {
    val bracketStack = Stack<Char>()
    val balancingCharacters = HashMap<Char, Char>()
    balancingCharacters['('] = ')'
    balancingCharacters['['] = ']'
    for (character in s) {
        if (balancingCharacters.containsKey(character)) bracketStack.push(character)
        if (balancingCharacters.containsValue(character)) {
            if (bracketStack.empty()) return false
            if (balancingCharacters[bracketStack.pop()] != character) return false
        }
    }
    return bracketStack.empty()
}

