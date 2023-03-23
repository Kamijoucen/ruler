package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.OperationDefine;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.CallOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IRClassValue;

public class DotEval implements BaseEval<DotNode> {

    private static final CallOperation callOperation = (CallOperation) OperationDefine.findOperation(TokenType.CALL);

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        TokenType dotType = node.getDotType();
        // todo 这个不能直接call rclass，要先检查本地fields，然后在检查rclass内
        IRClassValue classValue = scope.getCallChainPreviousValue().getRClass();
        // todo 这里应该是个fun 而不是
        // RClass 分为两种 可以添加成员，不能添加成员
        // 所有对象都有个 name._properties_.put("name", fun(str) -> str + 1)
        // _properties_为相同类型共同持有
        // 对于_properties_的修改，该类型下所有实例都会受影响

        // 对于RSON类型对象持有一个_fields_类型的字段，表示实例本身持有成员
        // 需要处理这种结构o.foo()。 dot时需要带上是谁的dot，call时，要看call的对象是否有dot原始对象

        if (dotType == TokenType.IDENTIFIER) {
            return classValue.getProperty(node.getName());
        } else if (dotType == TokenType.CALL) {
            // todo 直接get的函数不能指向self，需要重新设计
            BaseValue funValue = classValue.getProperty(node.getName());

            BaseValue[] baseValues = new BaseValue[node.getParam().size() + 2];
            baseValues[0] = scope.getCallChainPreviousValue();
            baseValues[1] = funValue;
            for (int i = 0; i < node.getParam().size(); i++) {
                baseValues[i + 2] = node.getParam().get(i).eval(context, scope);
            }
            // todo callOperation.compute(context, null);
            // todo 这里应该通过metadata getProp
            return callOperation.compute(context, baseValues);
        } else {
            throw SyntaxException.withSyntax("不支持的DOT调用类型:" + dotType);
        }
    }
}
