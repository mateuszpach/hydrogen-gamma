package model;

import model.terminalmodules.CountLetters;
import model.terminalmodules.CountWords;
import model.terminalmodules.LUDecomposer;
import model.terminalmodules.LongestCommonSubstring;

public enum TerminalModules {
    COUNT_LETTERS("count_letters", new CountLetters()),
    COUNT_WORDS("count_words", new CountWords()),
    LU_DECOMPOSITION("lu", new LUDecomposer()),
    LONGEST_COMMON_SUBSTRING("lcs", new LongestCommonSubstring());

    String name;
    TerminalModule module;

    TerminalModules(String name, TerminalModule module) {
        this.name = name;
        this.module = module;
    }
}
