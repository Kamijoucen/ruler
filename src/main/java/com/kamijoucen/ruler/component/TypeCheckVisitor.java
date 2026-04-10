package com.kamijoucen.ruler.component;
import com.kamijoucen.ruler.logic.typecheck.BinaryChecker;

import com.kamijoucen.ruler.domain.ast.expression.*;
import com.kamijoucen.ruler.domain.ast.factor.*;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.type.*;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class TypeCheckVisitor extends AbstractVisitor {

    private static final BinaryChecker binaryChecker = new BinaryChecker();
    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return IntegerType.INSTANCE;
    }

    @Override
    public BaseValue eval(DoubleNode node, Scope scope, RuntimeContext context) {
        return DoubleType.INSTANCE;
    }

    @Override
    public BaseValue eval(BoolNode node, Scope scope, RuntimeContext context) {
        return BoolType.INSTANCE;
    }

    @Override
    public BaseValue eval(StringNode node, Scope scope, RuntimeContext context) {
        return StringType.INSTANCE;
    }

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        return binaryChecker.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        return node.getExp().typeCheck(scope, context);
    }

    @Override
    public BaseValue eval(ArrayNode node, Scope scope, RuntimeContext context) {
        return ArrayType.INSTANCE;
    }

    @Override
    public BaseValue eval(NullNode node, Scope scope, RuntimeContext context) {
        return NullType.INSTANCE;
    }

    @Override
    public BaseValue eval(RsonNode node, Scope scope, RuntimeContext context) {
        return RsonType.INSTANCE;
    }

    @Override
    public BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        return StringType.INSTANCE;
    }

    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }

    @Override
    public BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }

    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }

    @Override
    public BaseValue eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }

    @Override
    public BaseValue eval(BreakNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }

    @Override
    public BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }

    @Override
    public BaseValue eval(CallNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public BaseValue eval(IndexNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public BaseValue eval(ClosureDefineNode node, Scope scope, RuntimeContext context) {
        return UnknownType.INSTANCE;
    }

    @Override
    public BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }

    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }

    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }

    @Override
    public BaseValue eval(RuleStatementNode node, Scope scope, RuntimeContext context) {
        return FailureType.INSTANCE;
    }
}
