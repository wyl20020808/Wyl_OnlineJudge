package com.wyl.backend.classes.contest.auxiliaryclass;

import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> words = new ArrayList<>();
}

public class Trie {
    TrieNode root = new TrieNode();

    public Trie(ArrayList<String> words) {
        for (String word : words) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
                node.words.add(word);
            }
        }
    }

    public List<String> findWordsWithPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return new ArrayList<>();
        }
        return node.words;
    }
}