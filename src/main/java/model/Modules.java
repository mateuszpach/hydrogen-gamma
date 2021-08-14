package model;

import model.modules.NumberAddition;
import model.modules.NumberMultiplication;

public enum Modules {
    NUMBER_ADD("+", NumberAddition.INSTANCE),
    NUMBER_MUL("*", NumberMultiplication.INSTANCE);

    String name;
    Module<?> module;

    Modules(String name, Module<?> module) {
        this.name = name;
        this.module = module;
    }
}
