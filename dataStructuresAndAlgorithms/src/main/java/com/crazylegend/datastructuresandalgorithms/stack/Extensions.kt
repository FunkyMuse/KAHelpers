package com.crazylegend.datastructuresandalgorithms.stack


fun <T> stackOf(vararg elements: T) = Stack.create(elements.asList())


fun String.checkForBalancedParentheses(): Boolean {
    val stack = Stack<Char>()

    for (character in this) {
        when (character) {
            '(' -> stack.push(character)
            ')' -> if (stack.isEmpty) return false else stack.pop()
        }
    }
    return stack.isEmpty
}
