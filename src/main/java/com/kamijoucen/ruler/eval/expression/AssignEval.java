package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.AssignNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.*;

public class AssignEval implements BaseEval<AssignNode> {

    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context, NodeVisitor visitor) {
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

        if (preValue.getType() == ValueType.ARRAY) {
            checkType(indexValue, ValueType.INTEGER, "Array index must be an integer");
            ArrayValue arrayValue = (ArrayValue) preValue;
            arrayValue.getValues().set((int) ((IntegerValue) indexValue).getValue(), value);
        } else if (preValue.getType() == ValueType.RSON) {
            checkType(indexValue, ValueType.STRING, "Object key must be a string");
            RsonValue rsonValue = (RsonValue) preValue;
            context.getConfiguration().getObjectAccessControlManager().modifyObject(rsonValue,
                    ((StringValue) indexValue).getValue(), value, context);
        } else {
            throw SyntaxException.withSyntax(preValue.getType() + " is not indexable");
        }
        return value;
    }

    private BaseValue evalDotOperation(AssignNode node, Scope scope, RuntimeContext context, BaseValue preValue) {
        if (preValue.getType() != ValueType.RSON) {
            throw SyntaxException.withSyntax(preValue.getType() + " is not indexable");
        }
        String fieldKey = ((NameNode) ((DotNode) node.getLhs()).getRhs()).name.name;
        BaseValue value = node.getRhs().eval(scope, context);
        context.getConfiguration().getObjectAccessControlManager().modifyObject(preValue, fieldKey, value, context);
        return value;
    }

    private void checkType(BaseValue value, ValueType expectedType, String errorMessage) {
        if (value.getType() != expectedType) {
            throw SyntaxException.withSyntax(errorMessage);
        }
    }
}
