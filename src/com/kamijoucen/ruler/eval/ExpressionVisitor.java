package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.env.DefaultScope;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface ExpressionVisitor {

    BaseValue eval(NameAST ast, Scope scope);

    BaseValue eval(IntegerAST ast, Scope scope);

    BaseValue eval(DoubleAST ast, Scope scope);

    BaseValue eval(BoolAST ast, Scope scope);

    BaseValue eval(StringAST ast, Scope scope);

    BaseValue eval(BinaryOperationAST ast, Scope scope);

}
