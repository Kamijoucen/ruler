package com.kamijoucen.ruler.logic.typecheck;

import com.kamijoucen.ruler.domain.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.domain.type.*;

public class BinaryChecker {

    public RulerType eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        RulerType lhs = node.getLhs().typeCheck(scope, context);
        RulerType rhs = node.getRhs().typeCheck(scope, context);

        if (lhs.getKind() == TypeKind.UNKNOWN || rhs.getKind() == TypeKind.UNKNOWN) {
            return UnknownType.INSTANCE;
        }

        TokenType op = node.getOp();
        String opName = node.getOpName();

        if (op == TokenType.EQ || op == TokenType.NE) {
            return BoolType.INSTANCE;
        }

        switch (op) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
                if (!lhs.isNumeric() || !rhs.isNumeric()) {
                    throw new SyntaxException(
                            "operator '" + opName + "' requires numeric types but got "
                                    + lhs.getKind() + " and " + rhs.getKind(),
                            node.getLocation());
                }
                return (lhs.getKind() == TypeKind.DOUBLE || rhs.getKind() == TypeKind.DOUBLE)
                        ? DoubleType.INSTANCE : IntegerType.INSTANCE;
            case STRING_ADD:
                return StringType.INSTANCE;
            case LT:
            case GT:
            case LE:
            case GE:
                if (!lhs.isNumeric() || !rhs.isNumeric()) {
                    throw new SyntaxException(
                            "comparison operator '" + opName + "' requires numeric types but got "
                                    + lhs.getKind() + " and " + rhs.getKind(),
                            node.getLocation());
                }
                return BoolType.INSTANCE;
            case AND:
            case OR:
                if (lhs.getKind() != TypeKind.BOOL || rhs.getKind() != TypeKind.BOOL) {
                    throw new SyntaxException(
                            "logical operator '" + opName + "' requires BOOL types but got "
                                    + lhs.getKind() + " and " + rhs.getKind(),
                            node.getLocation());
                }
                return BoolType.INSTANCE;
            default:
                return UnknownType.INSTANCE;
        }
    }

}
