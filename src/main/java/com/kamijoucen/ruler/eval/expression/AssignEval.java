package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.AssignNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.IntegerValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.RsonValue;
import com.kamijoucen.ruler.value.StringValue;
import com.kamijoucen.ruler.value.ValueType;

public class AssignEval implements BaseEval<AssignNode> {

    @Override
    public BaseValue eval(AssignNode node, Environment env, RuntimeContext context,
            NodeVisitor visitor) {
        BaseNode lhs = node.getLhs();
        if (lhs instanceof NameNode) {
            return evalVariableNode(node, env, context, (NameNode) lhs, visitor);
        } else if (lhs instanceof BinaryOperationNode) {
            return evalBinaryOperationNode(node, env, context, (BinaryOperationNode) lhs, visitor);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    // this is test text
    private BaseValue evalVariableNode(AssignNode node, Environment env, RuntimeContext context,
            NameNode variableNode, NodeVisitor visitor) {
        BaseValue value = node.getRhs() == null ? NullValue.INSTANCE : node.getRhs().eval(visitor);
        env.update(variableNode.name.name, value);
        // scope.update(variableNode.name.name, value);
        return value;
    }

    private BaseValue evalBinaryOperationNode(AssignNode node, Environment env,
            RuntimeContext context, BinaryOperationNode binaryNode, NodeVisitor visitor) {
        BaseValue preValue = binaryNode.getLhs().eval(visitor);

        if (binaryNode.getOp() == TokenType.INDEX) {
            return evalIndexOperation(node, env, context, binaryNode, preValue, visitor);
        } else if (binaryNode.getOp() == TokenType.DOT) {
            return evalDotOperation(node, env, context, preValue, visitor);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private BaseValue evalIndexOperation(AssignNode node, Environment env, RuntimeContext context,
            BinaryOperationNode binaryNode, BaseValue preValue, NodeVisitor visitor) {
        BaseValue indexValue = binaryNode.getRhs().eval(visitor);

        BaseValue value = node.getRhs().eval(visitor);

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

    private BaseValue evalDotOperation(AssignNode node, Environment env, RuntimeContext context,
            BaseValue preValue, NodeVisitor visitor) {
        if (preValue.getType() != ValueType.RSON) {
            throw SyntaxException.withSyntax(preValue.getType() + " is not indexable");
        }
        String fieldKey = ((NameNode) ((DotNode) node.getLhs()).getRhs()).name.name;
        BaseValue value = node.getRhs().eval(visitor);
        context.getConfiguration().getObjectAccessControlManager().modifyObject(preValue, fieldKey,
                value, context);
        return value;
    }

    private void checkType(BaseValue value, ValueType expectedType, String errorMessage) {
        if (value.getType() != expectedType) {
            throw SyntaxException.withSyntax(errorMessage);
        }
    }
}
