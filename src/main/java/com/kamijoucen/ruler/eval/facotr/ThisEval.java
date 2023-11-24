package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.facotr.ThisNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.Constant;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

public class ThisEval implements BaseEval<ThisNode> {
    @Override
    public BaseValue eval(ThisNode node, Scope scope, RuntimeContext context) {
        BaseValue baseValue = scope.find(Constant.THIS_ARG);
        if (baseValue == null) {
            return NullValue.INSTANCE;
        }
        return baseValue;
    }
}
