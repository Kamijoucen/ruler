package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.op.DotNode;
import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.ValueType;

public class DotAssignOperation implements AssignOperation {

    @Override
    public void assign(BaseValue preOperationValue, OperationNode call, BaseNode expression, Scope scope) {

        if (preOperationValue.getType() != ValueType.RSON) {
            throw SyntaxException.withSyntax(preOperationValue.getType() + "不是一个对象");
        }
        RsonValue rsonValue = (RsonValue) preOperationValue;

        DotNode dotNode = (DotNode) call;

        if (dotNode.getDotType() != TokenType.IDENTIFIER) {
            throw SyntaxException.withSyntax("不能为调用赋值");
        }

        BaseValue value = expression.eval(scope);

        rsonValue.getMataData().put(dotNode.getName(), value);
    }
}