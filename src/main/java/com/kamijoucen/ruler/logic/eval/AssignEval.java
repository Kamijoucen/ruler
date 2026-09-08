package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.AssignNode;
import com.kamijoucen.ruler.types.ast.DotNode;
import com.kamijoucen.ruler.types.ast.BinaryOperationNode;
import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.types.value.*;
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
        BaseValue value = node.getRhs() == null ? NullValue.INSTANCE : EvalVisitor.evaluate(node.getRhs(), scope, context);
        scope.update(variableNode.name.name, value);
        return value;
    }

    private BaseValue evalBinaryOperationNode(AssignNode node, Scope scope, RuntimeContext context,
            BinaryOperationNode binaryNode) {
        BaseValue preValue = EvalVisitor.evaluate(binaryNode.getLhs(), scope, context);

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
        BaseValue indexValue = EvalVisitor.evaluate(binaryNode.getRhs(), scope, context);
        BaseValue value = EvalVisitor.evaluate(node.getRhs(), scope, context);
        return PropertyAccessor.setIndexProperty(preValue, indexValue, value, context);
    }

    private BaseValue evalDotOperation(AssignNode node, Scope scope, RuntimeContext context, BaseValue preValue) {
        String fieldKey = ((NameNode) ((DotNode) node.getLhs()).getRhs()).name.name;
        BaseValue value = EvalVisitor.evaluate(node.getRhs(), scope, context);
        return PropertyAccessor.setProperty(preValue, fieldKey, value, context);
    }
}
