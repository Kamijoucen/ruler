package com.kamijoucen.ruler.ast;

import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.Value;
import com.kamijoucen.ruler.value.ValueType;

public interface BaseAST {

    Value eval(Scope scope);

}
