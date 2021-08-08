package model;

import model.modules.NumberAdd;
import model.modules.NumberMul;

public enum Modules {
    NUMBER_ADD("+", NumberAdd.INSTANCE),
    NUMBER_MUL("*", NumberMul.INSTANCE);

    String name;
    Module<?> module;

    Modules(String name, Module<?> module) {
        this.name = name;
        this.module = module;
    }
}
