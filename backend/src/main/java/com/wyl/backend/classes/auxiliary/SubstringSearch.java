package com.wyl.backend.classes.auxiliary;

import java.util.*;

public class SubstringSearch {
    private List<String> words;

    public SubstringSearch(List<String> words) {
        this.words = words;
    }

    private boolean containsInOrder(String word, String query) {
        int i = 0, j = 0;
        while (i < word.length() && j < query.length()) {
            if (word.charAt(i) == query.charAt(j)) {
                j++;
            }
            i++;
        }
        return j == query.length();
    }

    public List<String> search(String query) {
        List<String> result = new ArrayList<>();
        for (String word : words) {
            if (containsInOrder(word, query)) {
                result.add(word);
            }
        }
        return result;
    }

//    public static void main(String[] args) {
//        List<String> words = Arrays.asList("208891895", "2089814", "217");
//        SubstringSearch ss = new SubstringSearch(words);
//        System.out.println(ss.search("127")); // prints: []
//    }
}