package model.modules;

import model.Module;
import model.ModuleSet;
import model.TilesContainer;
import model.Variable;
import model.variables.NumericVariable;
import utils.Pair;

import java.util.ArrayList;

public class NumberOperations implements ModuleSet {
    @Override
    public ArrayList<Pair<String, Module<?>>> getModules() {
        ArrayList<Pair<String, Module<?>>> modules = new ArrayList<>();
        modules.add(new Pair<>("+", new NumberAdd()));
        modules.add(new Pair<>("*", new NumberMulti()));
        return modules;
    }

    public static class NumberMulti implements Module<NumericVariable> {
        @Override
        public NumericVariable execute(TilesContainer container, Variable<?>... args) {
            double sum = 1d;
            for (Variable<?> arg : args) {
                sum *= ((NumericVariable) arg).getValue();
            }
            return new NumericVariable(sum);
        }

        @Override
        public boolean verify(Variable<?>... args) {
            for (Variable<?> a : args) {
                try {
                    NumericVariable b = (NumericVariable) a;
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class NumberAdd implements Module<NumericVariable> {
        @Override
        public NumericVariable execute(TilesContainer container, Variable<?>... args) {
            double sum = 0d;
            for (Variable<?> arg : args) {
                sum += ((NumericVariable) arg).getValue();
            }
            return new NumericVariable(sum);
        }

        @Override
        public boolean verify(Variable<?>... args) {
            for (Variable<?> a : args) {
                try {
                    NumericVariable b = (NumericVariable) a;
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        }
    }
}
