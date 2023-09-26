package com.wyl.backend.classes.auxiliary;

import java.util.*;

public class SubstringSearch {
    private List<String> words;

    public SubstringSearch(List<String> words) {
        this.words = words;
    }

    private int containsInOrder(String word, String query) {
        int i = 0, j = 0;
        while (i < word.length() && j < query.length()) {
            if (word.charAt(i) == query.charAt(j)) {
                j++;
            }
            i++;
        }
        if (j == query.length()) {
            if (word.equals(query)) {
                return 3;
            } else if (word.startsWith(query)) {
                return 2;
            } else {
                return 1;
            }
        }
        return 0;
    }

    public List<String> search(String query) {
        List<String> result = new ArrayList<>();
        Map<String, Integer> matchDegree = new HashMap<>();
        for (String word : words) {
            int degree = containsInOrder(word, query);
            if (degree > 0) {
                result.add(word);
                matchDegree.put(word, degree);
            }
        }

        // 使用自定义的比较器对结果进行排序
        result.sort((a, b) -> matchDegree.get(b) - matchDegree.get(a));

        return result;
    }

//    public static void main(String[] args) {
//        List<String> words = Arrays.asList("208891895", "2089814", "217");
//        SubstringSearch ss = new SubstringSearch(words);
//        System.out.println(ss.search("127")); // prints: []
//    }
}