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
import com.kamijoucen.ruler.value.RClass;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;

public class DotEval implements BaseEval<DotNode> {

    private static final CallOperation callOperation = (CallOperation) OperationDefine.findOperation(TokenType.CALL);

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        BaseValue prevValue = scope.getCallChainPreviousValue();
        BaseValue callValue = null;
        if (prevValue.getType() == ValueType.RSON) {
            callValue = ((RsonValue) prevValue).getField(node.getName());
        }
        if (callValue == null) {
            RClass rClass = context.getConfiguration().getRClassManager().getClassValue(prevValue.getType());
            callValue = rClass.getProperty(node.getName());
            if (callValue == null) {
                callValue = NullValue.INSTANCE;
            }
        }
        TokenType dotType = node.getDotType();
        if (dotType == TokenType.CALL) {

            if (callValue.getType() != ValueType.FUNCTION
                    && callValue.getType() != ValueType.CLOSURE) {
                throw new UnsupportedOperationException(
                        "value '" + node.getName() + "' not is a function! " + callValue);
            }

            BaseValue[] baseValues = new BaseValue[node.getParam().size() + 2];
            // self
            baseValues[0] = scope.getCallChainPreviousValue();
            baseValues[1] = callValue;
            for (int i = 0; i < node.getParam().size(); i++) {
                baseValues[i + 2] = node.getParam().get(i).eval(context, scope);
            }
            return callOperation.invoke(context, , baseValues);
        } else if (dotType == TokenType.IDENTIFIER) {
            return callValue;
        } else {
            throw SyntaxException.withSyntax("dot calls not support this type:" + dotType);
        }
    }
}
