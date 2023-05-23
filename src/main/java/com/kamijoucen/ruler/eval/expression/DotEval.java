package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.RClass;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NullValue;

public class DotEval implements BaseEval<DotNode> {

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        BaseValue prevValue = node.getLhs().eval(scope, context);
        context.setCurrentSelfValue(prevValue);

        BaseNode nodeName = node.getRhs();
        if (!(nodeName instanceof NameNode)) {
            throw new IllegalArgumentException();
        }
        String callName = ((NameNode) nodeName).name.name;
        BaseValue callValue = null;
        if (prevValue.getType() == ValueType.RSON) {
            callValue = ((RsonValue) prevValue).getField(callName);
        }
        if (callValue == null) {
            RClass rClass = context.getConfiguration().getRClassManager().getClassValue(prevValue.getType());
            callValue = rClass.getProperty(callName);
            if (callValue == null) {
                callValue = NullValue.INSTANCE;
            }
        }
        return callValue;
    }
}
