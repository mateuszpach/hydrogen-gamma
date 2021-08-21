package hydrogengamma.model.modules;

import hydrogengamma.model.Module;
import hydrogengamma.model.TilesContainer;
import hydrogengamma.model.Variable;
import hydrogengamma.model.variables.NumericVariable;
import hydrogengamma.model.variables.TextVariable;

public class LevenshteinDistance implements Module<NumericVariable> {
    @Override
    public NumericVariable execute(TilesContainer container, Variable<?>... args) {
        String word1 = ((TextVariable) args[0]).getValue();
        String word2 = ((TextVariable) args[1]).getValue();

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

        return new NumericVariable(d[word1.length()][word2.length()]);
    }

    @Override
    public boolean verify(Variable<?>... args) {
        return args.length == 2 && args[0] instanceof TextVariable && args[1] instanceof TextVariable;
    }
}