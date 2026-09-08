package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.VariableDefineNode;
import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;

public class VariableEval implements BaseEval<VariableDefineNode> {
    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        NameNode lhs = (NameNode) node.getLhs();
        BaseValue defValue = scope.getByLocal(lhs.name.name);
        if (defValue != null) {
            throw new RulerRuntimeException("variable '" + lhs.name.name + "' already defined", lhs.getLocation());
        }
        if (node.getRhs() != null) {
            BaseValue rValue = EvalVisitor.evaluate(node.getRhs(), scope, context);
            scope.putLocal(lhs.name.name, rValue);
            return rValue;
        } else {
            scope.putLocal(lhs.name.name, NullValue.INSTANCE);
            return NullValue.INSTANCE;
        }
    }
}
