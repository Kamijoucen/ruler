package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.ValueType;

public class DotAssignOperation implements AssignOperation {

    @Override
    public BaseValue assign(BaseValue preOperationValue, OperationNode call, BaseNode expression, Scope scope,
                            RuntimeContext context) {
        DotNode dotNode = (DotNode) call;
        if (dotNode.getDotType() != TokenType.IDENTIFIER) {
            // todo 这里需要在编译期处理
            throw new UnsupportedOperationException("no assignment is allowed");
        }
        if (preOperationValue.getType() != ValueType.RSON) {
            throw SyntaxException.withSyntax(preOperationValue.getType() + " not is a rson object!");
        }
        BaseValue value = expression.eval(scope, context);
        // set rson field
        ((RsonValue) preOperationValue).putField(dotNode.getName(), value);
        return value;
    }
}
