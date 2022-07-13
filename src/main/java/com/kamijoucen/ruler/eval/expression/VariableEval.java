package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class VariableEval implements BaseEval<VariableDefineNode> {
    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        BaseNode name = node.getName();
        BaseValue value = node.getExpression().eval(context, scope);
        scope.defineLocal(((NameNode) name).name.name, value);
        return value;
    }
}
