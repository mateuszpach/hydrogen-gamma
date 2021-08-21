package hydrogengamma.model;


public class VarBox {
    Variable<?> var;

    VarBox(Variable<?> var) {
        this.var = var;
    }

    public Variable<?> getVar() {
        return this.var;
    }

}
