package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.MataValue;

import java.util.ArrayList;
import java.util.List;

public class DotEval implements BaseEval<DotNode> {
    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        TokenType dotType = node.getDotType();
        BaseValue operationValue = scope.getCallLinkPreviousValue();
        if (!(operationValue instanceof MataValue)) {
            throw SyntaxException.withSyntax(operationValue + "不支持进行'.'操作");
        }
        MataValue mataValue = (MataValue) operationValue;
        scope.putCurrentMataValue(mataValue);
        if (dotType == TokenType.IDENTIFIER) {
            return mataValue.getProperty(node.getName());
        } else if (dotType == TokenType.CALL) {
            List<BaseValue> values = new ArrayList<BaseValue>(node.getParam().size());
            for (BaseNode p : node.getParam()) {
                values.add(p.eval(context, scope));
            }
            return mataValue.invoke(context, node.getName(), values);
        } else {
            throw SyntaxException.withSyntax("不支持的DOT调用类型:" + dotType);
        }
    }
}
