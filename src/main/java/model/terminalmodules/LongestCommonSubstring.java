package model.terminalmodules;

import model.TerminalModule;
import model.TilesContainer;
import model.Variable;
import model.variables.TextVariable;
import vartiles.TableTile;

import java.util.ArrayList;

public class LongestCommonSubstring implements TerminalModule {

    @Override
    public void execute(TilesContainer container, Variable<?>... args) {
        String text1 = ((TextVariable) args[0]).getValue();
        String text2 = ((TextVariable) args[1]).getValue();

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

        container.addTile(new TableTile(result));
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return false;
    }
}