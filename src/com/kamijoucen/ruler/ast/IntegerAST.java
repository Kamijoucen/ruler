package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class IntegerAST implements BaseAST {

    public int value;

    public IntegerAST(int value) {
        this.value = value;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
