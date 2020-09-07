package com.crazylegend.datastructuresandalgorithms.problems

import java.util.*


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


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

