package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.OutNameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NullValue;

public class OutNameEval implements BaseEval<OutNameNode> {

    @Override
    public EvalResult eval(OutNameNode node, Scope scope, RuntimeContext context) {
        BaseValue value = context.findOutValue(node.name.name);
        if (value == null) {
            return NullValue.INSTANCE;
        }
        return value;
    }
}
