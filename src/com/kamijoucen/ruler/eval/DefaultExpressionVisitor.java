package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.operation.Operation;
import com.kamijoucen.ruler.runtime.BinaryDefine;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.NullValue;

public class DefaultExpressionVisitor implements ExpressionVisitor {

    @Override
    public BaseValue eval(NameAST ast, Scope scope) {
        BaseValue baseValue = scope.findValue(ast);
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
    public BaseValue eval(StringAST ast, Scope scope) {
        return new StringValue(ast.getValue());
    }

    @Override
    public BaseValue eval(BinaryOperationAST ast, Scope scope) {

        BaseValue val1 = ast.getExp1().eval(scope);
        BaseValue val2 = ast.getExp2().eval(scope);

        Operation operation = BinaryDefine.findOperation(ast.getOp());

        return operation.eval(val1, val2);
    }
}
