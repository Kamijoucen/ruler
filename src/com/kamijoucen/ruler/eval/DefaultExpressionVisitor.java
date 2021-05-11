package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.basic.Operation;
import com.kamijoucen.ruler.runtime.BinaryDefine;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
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

    @Override
    public BaseValue eval(BoolAST ast, Scope scope) {
        return new BoolValue(ast.getValue());
    }

    @Override
    public BaseValue eval(BinaryOperationAST ast, Scope scope) {

        BaseValue val1 = ast.getExp1().eval(scope);
        BaseValue val2 = ast.getExp2().eval(scope);

        Operation operation = BinaryDefine.findOperation(ast.getOp());

        return operation.eval(val1, val2);
    }
}
