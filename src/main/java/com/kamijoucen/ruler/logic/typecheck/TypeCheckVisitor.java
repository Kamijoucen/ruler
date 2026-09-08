package com.kamijoucen.ruler.logic.typecheck;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.AssignNode;
import com.kamijoucen.ruler.types.ast.BlockNode;
import com.kamijoucen.ruler.types.ast.CallNode;
import com.kamijoucen.ruler.types.ast.ClosureDefineNode;
import com.kamijoucen.ruler.types.ast.DefaultParamValNode;
import com.kamijoucen.ruler.types.ast.DotNode;
import com.kamijoucen.ruler.types.ast.ForEachStatementNode;
import com.kamijoucen.ruler.types.ast.IfStatementNode;
import com.kamijoucen.ruler.types.ast.ImportNode;
import com.kamijoucen.ruler.types.ast.IndexNode;
import com.kamijoucen.ruler.types.ast.InfixDefinitionNode;
import com.kamijoucen.ruler.types.ast.MatchCase;
import com.kamijoucen.ruler.types.ast.MatchNode;
import com.kamijoucen.ruler.types.ast.RuleStatementNode;
import com.kamijoucen.ruler.types.ast.VariableDefineNode;
import com.kamijoucen.ruler.types.ast.WhileStatementNode;
import com.kamijoucen.ruler.types.ast.ArrayNode;
import com.kamijoucen.ruler.types.ast.BinaryOperationNode;
import com.kamijoucen.ruler.types.ast.BoolNode;
import com.kamijoucen.ruler.types.ast.BreakNode;
import com.kamijoucen.ruler.types.ast.ContinueNode;
import com.kamijoucen.ruler.types.ast.DoubleNode;
import com.kamijoucen.ruler.types.ast.IntegerNode;
import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.types.ast.NullNode;
import com.kamijoucen.ruler.types.ast.OutNameNode;
import com.kamijoucen.ruler.types.ast.ReturnNode;
import com.kamijoucen.ruler.types.ast.RsonNode;
import com.kamijoucen.ruler.types.ast.StringInterpolationNode;
import com.kamijoucen.ruler.types.ast.StringNode;
import com.kamijoucen.ruler.types.ast.TypeOfNode;
import com.kamijoucen.ruler.types.ast.UnaryOperationNode;
import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.runtime.TypeScope;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.types.typing.ArrayType;
import com.kamijoucen.ruler.types.typing.BoolType;
import com.kamijoucen.ruler.types.typing.DoubleType;
import com.kamijoucen.ruler.types.typing.IntegerType;
import com.kamijoucen.ruler.types.typing.NullType;
import com.kamijoucen.ruler.types.typing.RsonType;
import com.kamijoucen.ruler.types.typing.RulerType;
import com.kamijoucen.ruler.types.typing.StringType;
import com.kamijoucen.ruler.types.typing.TypeKind;
import com.kamijoucen.ruler.types.typing.UnknownType;
import com.kamijoucen.ruler.logic.AbstractVisitor;

public class TypeCheckVisitor extends AbstractVisitor<RulerType> {

    private static final TypeCheckVisitor INSTANCE = new TypeCheckVisitor();

    public static RulerType check(BaseNode node, Scope scope, RuntimeContext context) {
        return node.accept(INSTANCE, scope, context);
    }

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
            RulerType expType = TypeCheckVisitor.check(node.getLhs(), scope, context);
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
        RulerType expType = TypeCheckVisitor.check(node.getExp(), scope, context);
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
                TypeCheckVisitor.check(block, scope, context);
            }
        } finally {
            context.setTypeScope(context.getTypeScope().getParent());
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        RulerType condType = TypeCheckVisitor.check(node.getCondition(), scope, context);
        if (condType.isKnown() && condType.getKind() != TypeKind.BOOL) {
            throw new SyntaxException(
                    "condition of 'if' statement must be BOOL but got " + condType.getKind(),
                    node.getCondition().getLocation());
        }
        TypeCheckVisitor.check(node.getThenBlock(), scope, context);
        if (node.getElseBlock() != null) {
            TypeCheckVisitor.check(node.getElseBlock(), scope, context);
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(AssignNode node, Scope scope, RuntimeContext context) {
        RulerType rhs = TypeCheckVisitor.check(node.getRhs(), scope, context);
        BaseNode lhs = node.getLhs();
        if (lhs instanceof NameNode) {
            context.getTypeScope().put(((NameNode) lhs).name.name, rhs);
        }
        return rhs;
    }

    @Override
    public RulerType eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        RulerType condType = TypeCheckVisitor.check(node.getCondition(), scope, context);
        if (condType.isKnown() && condType.getKind() != TypeKind.BOOL) {
            throw new SyntaxException(
                    "condition of 'while' statement must be BOOL but got " + condType.getKind(),
                    node.getCondition().getLocation());
        }
        TypeCheckVisitor.check(node.getBlock(), scope, context);
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        TypeCheckVisitor.check(node.getList(), scope, context);
        context.setTypeScope(new TypeScope(context.getTypeScope()));
        try {
            if (node.getLoopName() != null) {
                context.getTypeScope().put(node.getLoopName().name, UnknownType.INSTANCE);
            }
            TypeCheckVisitor.check(node.getBlock(), scope, context);
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
            TypeCheckVisitor.check(param, scope, context);
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(IndexNode node, Scope scope, RuntimeContext context) {
        TypeCheckVisitor.check(node.getRhs(), scope, context);
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
                TypeCheckVisitor.check(param, scope, context);
                if (param instanceof NameNode) {
                    context.getTypeScope().put(((NameNode) param).name.name, UnknownType.INSTANCE);
                } else if (param instanceof DefaultParamValNode) {
                    context.getTypeScope().put(((DefaultParamValNode) param).getName().name.name, UnknownType.INSTANCE);
                }
            }
            TypeCheckVisitor.check(node.getBlock(), scope, context);
        } finally {
            context.setTypeScope(context.getTypeScope().getParent());
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(ReturnNode node, Scope scope, RuntimeContext context) {
        for (BaseNode param : node.getParam()) {
            TypeCheckVisitor.check(param, scope, context);
        }
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        RulerType rhsType = TypeCheckVisitor.check(node.getRhs(), scope, context);
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
        TypeCheckVisitor.check(node.getAlias(), scope, context);
        TypeCheckVisitor.check(node.getBlock(), scope, context);
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(InfixDefinitionNode node, Scope scope, RuntimeContext context) {
        TypeCheckVisitor.check(node.getFunction(), scope, context);
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(DefaultParamValNode node, Scope scope, RuntimeContext context) {
        TypeCheckVisitor.check(node.getName(), scope, context);
        TypeCheckVisitor.check(node.getExp(), scope, context);
        return UnknownType.INSTANCE;
    }

    @Override
    public RulerType eval(MatchNode node, Scope scope, RuntimeContext context) {
        TypeCheckVisitor.check(node.getScrutinee(), scope, context);
        for (MatchCase matchCase : node.getCases()) {
            context.setTypeScope(new TypeScope(context.getTypeScope()));
            try {
                if (matchCase.getGuard() != null) {
                    TypeCheckVisitor.check(matchCase.getGuard(), scope, context);
                }
                TypeCheckVisitor.check(matchCase.getBody(), scope, context);
            } finally {
                context.setTypeScope(context.getTypeScope().getParent());
            }
        }
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
