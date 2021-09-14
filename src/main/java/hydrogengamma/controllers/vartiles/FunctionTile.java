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

        StringBuilder b = new StringBuilder();
        char[] f = function.getValue().toCharArray();
        int i = 0;

        while (i < f.length) {
            b.append(f[i]);
            if (f[i] == '^') {
                b.append('{');
                int open = 0;
                int j = i + 1;
                while (j < f.length) {
                    b.append(f[j]);
                    if (f[j] == '(')
                        open++;
                    if (f[j] == ')')
                        open--;
                    if (open == 0) {
                        b.append('}');
                        break;
                    }
                    j++;
                }
                i = j + 1;
            }
            else {
                i++;
            }
        }
        return b.toString();
    }
}
