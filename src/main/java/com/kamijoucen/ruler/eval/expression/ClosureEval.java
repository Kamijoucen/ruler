package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.ValueType;

import java.util.List;

public class ClosureEval implements BaseEval<ClosureDefineNode> {
    @Override
    public BaseValue eval(ClosureDefineNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParam();
        String funName = node.getName();
        ClosureValue closureValue = new ClosureValue(scope, param, node.getBlock());
        if (funName != null) {
            scope.defineLocal(funName, closureValue);
        }
        return closureValue;
    }
}
