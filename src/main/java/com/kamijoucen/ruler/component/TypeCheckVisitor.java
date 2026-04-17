package com.kamijoucen.ruler.component;
import com.kamijoucen.ruler.logic.typecheck.BinaryChecker;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.*;
import com.kamijoucen.ruler.domain.ast.factor.*;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.runtime.TypeScope;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.domain.type.*;

public class TypeCheckVisitor extends AbstractVisitor<RulerType> {

    private static final BinaryChecker binaryChecker = new BinaryChecker();

    @Override
    public RulerType eval(NameNode node, Scope scope, RuntimeContext context) {
        RulerType type = context.getTypeScope().find(node.name.name);
        return type == null ? UnknownType.INSTANCE : type;
    }

    @Override
    public RulerType eval(OutNameNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return IntegerType.INSTANCE;
    }

    @Override
    public RulerType eval(DoubleNode node, Scope scope, RuntimeContext context) {
        return DoubleType.INSTANCE;
    }

    @Override
    public RulerType eval(BoolNode node, Scope scope, RuntimeContext context) {
        return BoolType.INSTANCE;
    }

    @Override
    public RulerType eval(StringNode node, Scope scope, RuntimeContext context) {
        return StringType.INSTANCE;
    }

    @Override
    public RulerType eval(StringInterpolationNode node, Scope scope, RuntimeContext context) {
        return StringType.INSTANCE;
    }

    @Override
    public RulerType eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        if (node.getOp() == TokenType.NOT) {
            RulerType expType = node.getLhs().typeCheck(scope, context);
            if (expType.getKind() == TypeKind.UNKNOWN) {
                return UnknownType.INSTANCE;
            }
            if (expType.getKind() != TypeKind.BOOL) {
                throw new SyntaxException(
                        "operator '!' requires BOOL but got " + expType.getKind(),
                        node.getLocation());
            }
            return BoolType.INSTANCE;
        }
        return binaryChecker.eval(node, scope, context);
    }

    @Override
    public RulerType eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        RulerType expType = node.getExp().typeCheck(scope, context);
        if (expType.getKind() == TypeKind.UNKNOWN) {
            return UnknownType.INSTANCE;
        }
        if (node.getOp() == TokenType.NOT) {
            if (expType.getKind() != TypeKind.BOOL) {
                throw new SyntaxException(
                        "unary operator '!' requires BOOL but got " + expType.getKind(),
                        node.getLocation());
            }
            return BoolType.INSTANCE;
        }
        if (node.getOp() == TokenType.SUB
                || node.getOp() == TokenType.ADD) {
            if (!expType.isNumeric()) {
                throw new SyntaxException(
                        "unary operator '" + unaryOpSymbol(node.getOp())
                                + "' requires numeric type but got " + expType.getKind(),
                        node.getLocation());
            }
            return expType;
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(ArrayNode node, Scope scope, RuntimeContext context) {
        return ArrayType.INSTANCE;
    }

    @Override
    public RulerType eval(NullNode node, Scope scope, RuntimeContext context) {
        return NullType.INSTANCE;
    }

    @Override
    public RulerType eval(RsonNode node, Scope scope, RuntimeContext context) {
        return RsonType.INSTANCE;
    }

    @Override
    public RulerType eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        return StringType.INSTANCE;
    }

    @Override
    public RulerType eval(BlockNode node, Scope scope, RuntimeContext context) {
        context.setTypeScope(new TypeScope(context.getTypeScope()));
        try {
            for (BaseNode block : node.getBlocks()) {
                block.typeCheck(scope, context);
            }
        } finally {
            context.setTypeScope(context.getTypeScope().getParent());
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        RulerType condType = node.getCondition().typeCheck(scope, context);
        if (condType.isKnown() && condType.getKind() != TypeKind.BOOL) {
            throw new SyntaxException(
                    "condition of 'if' statement must be BOOL but got " + condType.getKind(),
                    node.getCondition().getLocation());
        }
        node.getThenBlock().typeCheck(scope, context);
        if (node.getElseBlock() != null) {
            node.getElseBlock().typeCheck(scope, context);
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(AssignNode node, Scope scope, RuntimeContext context) {
        RulerType rhs = node.getRhs().typeCheck(scope, context);
        BaseNode lhs = node.getLhs();
        if (lhs instanceof NameNode) {
            context.getTypeScope().put(((NameNode) lhs).name.name, rhs);
        }
        return rhs;
    }

    @Override
    public RulerType eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        RulerType condType = node.getCondition().typeCheck(scope, context);
        if (condType.isKnown() && condType.getKind() != TypeKind.BOOL) {
            throw new SyntaxException(
                    "condition of 'while' statement must be BOOL but got " + condType.getKind(),
                    node.getCondition().getLocation());
        }
        node.getBlock().typeCheck(scope, context);
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        node.getList().typeCheck(scope, context);
        context.setTypeScope(new TypeScope(context.getTypeScope()));
        try {
            if (node.getLoopName() != null) {
                context.getTypeScope().put(node.getLoopName().name, UnknownType.INSTANCE);
            }
            node.getBlock().typeCheck(scope, context);
        } finally {
            context.setTypeScope(context.getTypeScope().getParent());
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(BreakNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(ContinueNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(CallNode node, Scope scope, RuntimeContext context) {
        for (BaseNode param : node.getParams()) {
            param.typeCheck(scope, context);
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(IndexNode node, Scope scope, RuntimeContext context) {
        node.getRhs().typeCheck(scope, context);
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(DotNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(ClosureDefineNode node, Scope scope, RuntimeContext context) {
        context.setTypeScope(new TypeScope(context.getTypeScope()));
        try {
            for (BaseNode param : node.getParam()) {
                param.typeCheck(scope, context);
                if (param instanceof NameNode) {
                    context.getTypeScope().put(((NameNode) param).name.name, UnknownType.INSTANCE);
                } else if (param instanceof DefaultParamValNode) {
                    context.getTypeScope().put(((DefaultParamValNode) param).getName().name.name, UnknownType.INSTANCE);
                }
            }
            node.getBlock().typeCheck(scope, context);
        } finally {
            context.setTypeScope(context.getTypeScope().getParent());
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(ReturnNode node, Scope scope, RuntimeContext context) {
        for (BaseNode param : node.getParam()) {
            param.typeCheck(scope, context);
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        RulerType rhsType = node.getRhs().typeCheck(scope, context);
        BaseNode lhs = node.getLhs();
        if (lhs instanceof NameNode) {
            context.getTypeScope().put(((NameNode) lhs).name.name, rhsType);
        }
        return rhsType;
    }

    @Override
    public RulerType eval(ImportNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(RuleStatementNode node, Scope scope, RuntimeContext context) {
        node.getAlias().typeCheck(scope, context);
        node.getBlock().typeCheck(scope, context);
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(InfixDefinitionNode node, Scope scope, RuntimeContext context) {
        node.getFunction().typeCheck(scope, context);
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(DefaultParamValNode node, Scope scope, RuntimeContext context) {
        node.getName().typeCheck(scope, context);
        node.getExp().typeCheck(scope, context);
        return UnknownType.INSTANCE;
    }

    private String unaryOpSymbol(TokenType op) {
        if (op == TokenType.ADD) {
            return "+";
        }
        if (op == TokenType.SUB) {
            return "-";
        }
        if (op == TokenType.NOT) {
            return "!";
        }
        return op.name();
    }

}
