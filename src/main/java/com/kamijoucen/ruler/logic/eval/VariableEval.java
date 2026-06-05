package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;

public class VariableEval implements BaseEval<VariableDefineNode> {
    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        NameNode lhs = (NameNode) node.getLhs();
        BaseValue defValue = scope.getByLocal(lhs.name.name);
        if (defValue != null) {
            throw new RulerRuntimeException("variable '" + lhs.name.name + "' already defined", lhs.getLocation());
        }
        if (node.getRhs() != null) {
            BaseValue rValue = node.getRhs().eval(scope, context);
            scope.putLocal(lhs.name.name, rValue);
            return rValue;
        } else {
            scope.putLocal(lhs.name.name, NullValue.INSTANCE);
            return NullValue.INSTANCE;
        }
    }
}
