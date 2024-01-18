package com.funkymuse.datastructuresandalgorithms.trie

fun Trie<Char>.insert(string: String) {
    insert(string.toList())
}

fun Trie<Char>.contains(string: String): Boolean {
    return contains(string.toList())
}

fun Trie<Char>.remove(string: String) {
    remove(string.toList())
}

fun Trie<Char>.collections(prefix: String): List<String> {
    return collections(prefix.toList()).map { it.joinToString(separator = "") }
}
