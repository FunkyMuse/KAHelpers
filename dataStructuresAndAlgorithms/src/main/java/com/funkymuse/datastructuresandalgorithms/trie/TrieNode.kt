package com.funkymuse.datastructuresandalgorithms.trie

class TrieNode<Key>(var key: Key?, var parent: TrieNode<Key>?) {

    val children: HashMap<Key, TrieNode<Key>> = HashMap()

    var isTerminating = false

}
