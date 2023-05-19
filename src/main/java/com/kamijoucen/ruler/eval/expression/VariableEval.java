package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.EvalResult;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NullValue;

public class VariableEval implements BaseEval<VariableDefineNode> {
    @Override
    public EvalResult eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        NameNode lhs = (NameNode) node.getLhs();
        BaseValue defValue = scope.getByLocal(lhs.name.name);
        if (defValue != null) {
            throw SyntaxException.withSyntax("", lhs.getLocation());
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
