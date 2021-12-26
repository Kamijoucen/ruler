package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.expression.*;
import com.kamijoucen.ruler.ast.facotr.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class TypeCheckVisitor extends AbstractVisitor {

    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(DoubleNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(BoolNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(StringNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(LogicBinaryOperationNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ArrayNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(NullNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(RsonNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ThisNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(LoopBlockNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(BreakNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(CallNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(IndexNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(CallLinkNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ClosureDefineNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }
}
