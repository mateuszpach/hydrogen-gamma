package model.modules;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public interface TextAnalysis {
    static Map<String, Integer> countWords(String text) {
        String[] words = text.trim().replaceAll("[^a-zA-Z ]", " ").split("\\s+");
        Map<String, Integer> wordsFreq = new TreeMap<>();
        for (String word : words) {
            Integer oldValue = wordsFreq.getOrDefault(word, 0);
            wordsFreq.put(word, oldValue + 1);
        }
        return wordsFreq;
    }

    static Map<Character, Integer> countLetters(String text) {
        text = text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        Map<Character, Integer> lettersFreq = new TreeMap<>();
        for (char letter : text.toCharArray()) {
            if (letter != ' ') {
                Integer oldValue = lettersFreq.getOrDefault(letter, 0);
                lettersFreq.put(letter, oldValue + 1);
            }
        }
        return lettersFreq;
    }

    static Integer levenshteinDistance(String word1, String word2) {
        // Wagner–Fischer algorithm
        // for all i and j, d[i,j] will hold the Levenshtein distance between
        // the first i characters of s and the first j characters of t
        // note that d has (m+1)*(n+1) values
        int[][] d = new int[word1.length() + 1][word2.length() + 1];

        // source prefixes can be transformed into empty string by
        // dropping all characters
        for (int i = 0; i < word1.length(); i++) {
            d[i + 1][0] = i + 1;
        }

        // target prefixes can be reached from empty source prefix
        // by inserting every character
        for (int i = 0; i < word2.length(); i++) {
            d[0][i + 1] = i + 1;
        }

        for (int j = 1; j <= word2.length(); j++) {
            for (int i = 1; i <= word1.length(); i++) {
                int substitutionCost = 1;
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) substitutionCost = 0;
                d[i][j] = Integer.MAX_VALUE;
                d[i][j] = Math.min(d[i][j], d[i - 1][j] + 1);   // deletion
                d[i][j] = Math.min(d[i][j], d[i][j - 1] + 1);   // insertion
                d[i][j] = Math.min(d[i][j], d[i - 1][j - 1] + substitutionCost); // substitution
            }
        }

        return d[word1.length()][word2.length()];
    }

    static ArrayList<String> longestCommonSubstrings(String text1, String text2) {
        int[][] prefixLCS = new int[text1.length() + 1][text2.length() + 1];
        int longestLength = 0;
        ArrayList<String> result = new ArrayList<>();

        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    if (i == 1 || j == 1) {
                        prefixLCS[i][j] = 1;
                    } else {
                        prefixLCS[i][j] = prefixLCS[i - 1][j - 1] + 1;
                    }
                    if (prefixLCS[i][j] > longestLength) {
                        longestLength = prefixLCS[i][j];
                        result.clear();
                        result.add(text1.substring(i - longestLength, i));
                    } else if (prefixLCS[i][j] == longestLength) {
                        result.add(text1.substring(i - longestLength, i));
                    }
                } else {
                    prefixLCS[i][j] = 0;
                }
            }
        }

        return result;
    }
}
