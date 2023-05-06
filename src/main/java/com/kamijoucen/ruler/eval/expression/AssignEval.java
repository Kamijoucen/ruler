package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.AssignNode;
import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.NullValue;

public class AssignEval implements BaseEval<AssignNode> {

    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context) {
        BaseNode lhs = node.getLhs();
        // 直接更新变量
        if (lhs instanceof NameNode) {
            String varName = ((NameNode) lhs).name.name;
            BaseValue value;
            if (node.getRhs() == null) {
                value = NullValue.INSTANCE;
            } else {
                value = node.getRhs().eval(scope, context);
            }
            scope.update(varName, value);
            return value;
        } else if (lhs instanceof BinaryOperation) {
            BinaryOperationNode binaryNode = (BinaryOperationNode) lhs;
            BaseValue preValue = binaryNode.getLhs().eval(scope, context);
            if (binaryNode.getOp() == TokenType.INDEX) {
                // todo name["name"]
                if (preValue.getType() != ValueType.ARRAY) {
                    throw SyntaxException.withSyntax(preValue.getType() + " not is array");
                }
                ArrayValue arrayValue = (ArrayValue) preValue;

                BaseValue indexValue = binaryNode.getRhs().eval(scope, context);
                if (indexValue.getType() != ValueType.INTEGER) {
                    throw SyntaxException.withSyntax("数组的索引必须是数字");
                }
                return arrayValue.getValues().get((int) ((IntegerValue) indexValue).getValue());
            } else if (binaryNode.getOp() == TokenType.DOT) {
                if (preValue.getType() == ValueType.RSON) {
                    BaseValue value = node.getRhs().eval(scope, context);
                    ((RsonValue) preValue).putField(null, value);
                    return value;
                } else {
                    // TODO update meta data
                    throw new UnsupportedOperationException();
                }
            } else {
                throw new UnsupportedOperationException();
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
