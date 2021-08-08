package model;

import model.terminalmodules.CountLetters;
import model.terminalmodules.CountWords;
import model.terminalmodules.LongestCommonSubstring;

public enum TerminalModules {
    COUNT_LETTERS("count_letters", CountLetters.INSTANCE),
    COUNT_WORDS("count_words", CountWords.INSTANCE),
    LONGEST_COMMON_SUBSTRING("lcs", LongestCommonSubstring.INSTANCE);

    String name;
    TerminalModule module;

    TerminalModules(String name, TerminalModule module) {
        this.name = name;
        this.module = module;
    }
}
