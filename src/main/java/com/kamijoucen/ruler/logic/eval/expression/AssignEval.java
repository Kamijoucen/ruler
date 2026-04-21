package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.AssignNode;
import com.kamijoucen.ruler.domain.ast.expression.DotNode;
import com.kamijoucen.ruler.domain.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.domain.value.*;
import com.kamijoucen.ruler.logic.property.PropertyAccessor;

public class AssignEval implements BaseEval<AssignNode> {

    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context) {
        BaseNode lhs = node.getLhs();
        if (lhs instanceof NameNode) {
            return evalVariableNode(node, scope, context, (NameNode) lhs);
        } else if (lhs instanceof BinaryOperationNode) {
            return evalBinaryOperationNode(node, scope, context, (BinaryOperationNode) lhs);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private BaseValue evalVariableNode(AssignNode node, Scope scope, RuntimeContext context, NameNode variableNode) {
        BaseValue value = node.getRhs() == null ? NullValue.INSTANCE : node.getRhs().eval(scope, context);
        scope.update(variableNode.name.name, value);
        return value;
    }

    private BaseValue evalBinaryOperationNode(AssignNode node, Scope scope, RuntimeContext context,
            BinaryOperationNode binaryNode) {
        BaseValue preValue = binaryNode.getLhs().eval(scope, context);

        if (binaryNode.getOp() == TokenType.INDEX) {
            return evalIndexOperation(node, scope, context, binaryNode, preValue);
        } else if (binaryNode.getOp() == TokenType.DOT) {
            return evalDotOperation(node, scope, context, preValue);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private BaseValue evalIndexOperation(AssignNode node, Scope scope, RuntimeContext context,
            BinaryOperationNode binaryNode, BaseValue preValue) {
        BaseValue indexValue = binaryNode.getRhs().eval(scope, context);
        BaseValue value = node.getRhs().eval(scope, context);
        return PropertyAccessor.setIndexProperty(preValue, indexValue, value, context);
    }

    private BaseValue evalDotOperation(AssignNode node, Scope scope, RuntimeContext context, BaseValue preValue) {
        String fieldKey = ((NameNode) ((DotNode) node.getLhs()).getRhs()).name.name;
        BaseValue value = node.getRhs().eval(scope, context);
        return PropertyAccessor.setProperty(preValue, fieldKey, value, context);
    }
}
