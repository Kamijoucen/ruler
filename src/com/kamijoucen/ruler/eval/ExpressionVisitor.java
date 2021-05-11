package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.BinaryOperationAST;
import com.kamijoucen.ruler.ast.DoubleAST;
import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.ast.IntegerAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface ExpressionVisitor {

    BaseValue eval(NameAST ast, Scope scope);

    BaseValue eval(IntegerAST ast, Scope scope);

    BaseValue eval(DoubleAST ast, Scope scope);

    BaseValue eval(BinaryOperationAST ast, Scope scope);

}
