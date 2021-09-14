package hydrogengamma.controllers.vartiles;

import hydrogengamma.model.variables.FunctionVariable;

public class FunctionTile extends DefaultTile {

    private final FunctionVariable function;

    public FunctionTile(FunctionVariable func, String label) {
        super(label);
        function = func;
    }

    @Override
    public String getContent() {
        return '$' + valueWithLatexExponents() + '$';
    }

    private String valueWithLatexExponents() {
        char[] f = function.getValue().toCharArray();
        int i = 0;
        while (i < f.length) {
            if (f[i] == '^') {
                int open = 0;
                int j = i + 1;
                while (j < f.length) {
                    if (f[j] == '(')
                        open++;
                    if (f[j] == ')')
                        open--;
                    if (open == 0) {
                        f[i + 1] = '{';
                        f[j] = '}';
                        i = j + 1;
                        break;
                    }
                    j++;
                }
            }
            else {
                i++;
            }
        }
        return String.valueOf(f);
    }
}
