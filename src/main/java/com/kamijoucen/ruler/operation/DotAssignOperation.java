package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.common.RMetaInfo;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.AbstractRClassValue;
import com.kamijoucen.ruler.value.BaseValue;

public class DotAssignOperation implements AssignOperation {

    @Override
    public void assign(BaseValue preOperationValue, OperationNode call, BaseNode expression, Scope scope,
            RuntimeContext context) {
        if (!(preOperationValue instanceof AbstractRClassValue)) {
            throw SyntaxException.withSyntax(preOperationValue.getType() + "不是一个对象");
        }
        AbstractRClassValue rsonValue = (AbstractRClassValue) preOperationValue;
        DotNode dotNode = (DotNode) call;
        if (dotNode.getDotType() != TokenType.IDENTIFIER) {
            throw SyntaxException.withSyntax("不能为调用赋值");
        }
        RMetaInfo mataData = rsonValue.getClassInfo();
        BaseValue value = expression.eval(context, scope);
        mataData.put(dotNode.getName(), value);
    }
}
