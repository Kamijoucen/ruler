package com.kamijoucen.ruler.eval.facotr;

import com.kamijoucen.ruler.ast.statement.ContinueNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.ContinueValue;

public class ContinueEval implements BaseEval<ContinueNode> {
    @Override
    public BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context) {
        return ContinueValue.INSTANCE;
    }
}
