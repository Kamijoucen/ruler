package com.kamijoucen.ruler.typecheck;

import com.kamijoucen.ruler.ast.expression.AssignNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.CallLinkNode;
import com.kamijoucen.ruler.ast.expression.CallNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.expression.IfStatementNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.expression.IndexNode;
import com.kamijoucen.ruler.ast.expression.LoopBlockNode;
import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.expression.WhileStatementNode;
import com.kamijoucen.ruler.ast.facotr.ArrayNode;
import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.ast.facotr.BoolNode;
import com.kamijoucen.ruler.ast.facotr.BreakNode;
import com.kamijoucen.ruler.ast.facotr.ContinueNode;
import com.kamijoucen.ruler.ast.facotr.DoubleNode;
import com.kamijoucen.ruler.ast.facotr.IntegerNode;
import com.kamijoucen.ruler.ast.facotr.LogicBinaryOperationNode;
import com.kamijoucen.ruler.ast.facotr.NameNode;
import com.kamijoucen.ruler.ast.facotr.NullNode;
import com.kamijoucen.ruler.ast.facotr.OutNameNode;
import com.kamijoucen.ruler.ast.facotr.ReturnNode;
import com.kamijoucen.ruler.ast.facotr.RsonNode;
import com.kamijoucen.ruler.ast.facotr.StringNode;
import com.kamijoucen.ruler.ast.facotr.ThisNode;
import com.kamijoucen.ruler.ast.facotr.TypeOfNode;
import com.kamijoucen.ruler.ast.facotr.UnaryOperationNode;
import com.kamijoucen.ruler.common.AbstractVisitor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.type.BoolType;
import com.kamijoucen.ruler.type.DoubleType;
import com.kamijoucen.ruler.type.IntegerType;
import com.kamijoucen.ruler.type.StringType;
import com.kamijoucen.ruler.type.UnknowType;
import com.kamijoucen.ruler.value.BaseValue;

public class TypeCheckVisitor extends AbstractVisitor {

    private static final BinaryChecker binaryChecker = new BinaryChecker();

    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        return UnknowType.INSTANCE;
    }

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        return UnknowType.INSTANCE;
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
