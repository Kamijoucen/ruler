package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.Value;

import java.util.List;

public class CallAST implements BaseAST {

    public final NameAST name;

    public final List<BaseAST> param;

    public CallAST(NameAST name, List<BaseAST> param) {
        this.name = name;
        this.param = param;
    }

    @Override
    public Value eval(Scope scope) {
        return null;
    }
}
