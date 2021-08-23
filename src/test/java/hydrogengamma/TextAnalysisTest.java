// TODO: update tests Mateusz

//class TextAnalysisTest {
//
//    @Test
//    void countWords() {
//        String regular = "Lorem ipsum lorem ipsum dolor sit amet amet amet";
//        String spaces = " Lorem    lorem dolor sit        ";
//        String specialChars = "Lorem $ ip?sum dolor sit \n amet,  ,, amet!";
//
//        Map<String, Integer> regularExpected = new TreeMap<>();
//        regularExpected.put("Lorem", 1);
//        regularExpected.put("ipsum", 2);
//        regularExpected.put("lorem", 1);
//        regularExpected.put("dolor", 1);
//        regularExpected.put("sit", 1);
//        regularExpected.put("amet", 3);
//
//        Map<String, Integer> spacesExpected = new TreeMap<>();
//        spacesExpected.put("Lorem", 1);
//        spacesExpected.put("lorem", 1);
//        spacesExpected.put("dolor", 1);
//        spacesExpected.put("sit", 1);
//
//        Map<String, Integer> specialCharsExpected = new TreeMap<>();
//        specialCharsExpected.put("Lorem", 1);
//        specialCharsExpected.put("ip", 1);
//        specialCharsExpected.put("sum", 1);
//        specialCharsExpected.put("dolor", 1);
//        specialCharsExpected.put("sit", 1);
//        specialCharsExpected.put("amet", 2);
//
//        Map<String, Integer> regularResult = TextAnalysis.countWords(regular);
//        Map<String, Integer> spacesResult = TextAnalysis.countWords(spaces);
//        Map<String, Integer> specialCharsResult = TextAnalysis.countWords(specialChars);
//
//        assertEquals(regularExpected, regularResult);
//        assertEquals(spacesExpected, spacesResult);
//        assertEquals(specialCharsExpected, specialCharsResult);
//    }
//
//    @Test
//    void countLetters() {
//        String text = " qwerty!@#\nQWErty123 .  aaaaaa";
//
//        Map<Character, Integer> textExpected = new TreeMap<>();
//        textExpected.put('q', 2);
//        textExpected.put('w', 2);
//        textExpected.put('e', 2);
//        textExpected.put('r', 2);
//        textExpected.put('t', 2);
//        textExpected.put('y', 2);
//        textExpected.put('a', 6);
//
//        Map<Character, Integer> textResult = TextAnalysis.countLetters(text);
//
//        assertEquals(textExpected, textResult);
//    }
//
//    @Test
//    void levenshteinDistance() {
//        String word1 = "Sunday";
//        String word2 = "Saturday";
//
//        int result = TextAnalysis.levenshteinDistance(word1, word2);
//
//        assertEquals(3, result);
//    }
//
//    @Test
//    void longestCommonSubstrings() {
//        String p1 = "zXDXDXDXDzxczcxzSaturdayczxc";
//        String p2 = "abcSaturdaySundayXDXDXDXD";
//        String q1 = "abaabaab";
//        String q2 = "xdabaabxdxdabaab";
//
//        ArrayList<String> expectedP = new ArrayList<>();
//        expectedP.add("XDXDXDXD");
//        expectedP.add("Saturday");
//
//        ArrayList<String> expectedQ = new ArrayList<>();
//        expectedQ.add("abaab");
//        expectedQ.add("abaab");
//        expectedQ.add("abaab");
//        expectedQ.add("abaab");
//
//        ArrayList<String> resultP = TextAnalysis.longestCommonSubstrings(p1, p2);
//        ArrayList<String> resultQ = TextAnalysis.longestCommonSubstrings(q1, q2);
//
//        assertEquals(expectedP, resultP);
//        assertEquals(expectedQ, resultQ);
//    }
//}