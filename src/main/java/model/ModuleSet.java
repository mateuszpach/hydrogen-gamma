package model;

import model.variables.NumericVariable;
import utils.Pair;

import java.util.ArrayList;

public interface ModuleSet {
    public ArrayList<Pair<String, Module<?>>> getModules();
}
