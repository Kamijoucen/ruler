package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;

public class VariableEval implements BaseEval<VariableDefineNode> {
    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context, NodeVisitor visitor) {
        NameNode lhs = (NameNode) node.getLhs();
        BaseValue defValue = scope.getByLocal(lhs.name.name);
        if (defValue != null) {
            throw SyntaxException.withSyntax("variable '" + lhs.name.name + "' has been defined", lhs.getLocation());
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
