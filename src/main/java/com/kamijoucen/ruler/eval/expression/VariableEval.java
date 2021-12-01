package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.ast.statement.VariableDefineNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NoneValue;

public class VariableEval implements BaseEval<VariableDefineNode> {
    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        BaseNode name = node.getName();
        BaseValue value = node.getExpression().eval(context, scope);
        scope.defineLocal(((NameNode) name).name.name, value);
        return NoneValue.INSTANCE;
    }
}
