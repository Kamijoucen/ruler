package com.kamijoucen.ruler.eval.impl;

import com.kamijoucen.ruler.ast.DoubleAST;
import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.ast.IntegerAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.eval.ExpressionVisitor;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.DoubleValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.constant.NullValue;

public class DefaultExpressionVisitor implements ExpressionVisitor {

    @Override
    public BaseValue eval(NameAST ast, Scope scope) {
        BaseValue baseValue = scope.find(ast);
        if (baseValue == null) {
            return NullValue.INSTANCE;
        }
        return baseValue;
    }

    @Override
    public BaseValue eval(IntegerAST ast, Scope scope) {
        return new IntegerValue(ast.getValue());
    }

    @Override
    public BaseValue eval(DoubleAST ast, Scope scope) {
        return new DoubleValue(ast.getValue());
    }

}
