package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.ast.expression.*;
import com.kamijoucen.ruler.ast.facotr.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface NodeVisitor {

    BaseValue eval(NameNode node, Scope scope, RuntimeContext context);

    BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context);

    BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context);

    BaseValue eval(DoubleNode node, Scope scope, RuntimeContext context);

    BaseValue eval(BoolNode node, Scope scope, RuntimeContext context);

    BaseValue eval(StringNode node, Scope scope, RuntimeContext context);

    BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context);

    BaseValue eval(CallNode2 node, Scope scope, RuntimeContext context);

    BaseValue eval(LogicBinaryOperationNode node, Scope scope, RuntimeContext context);

    BaseValue eval(UnaryOperationNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ArrayNode node, Scope scope, RuntimeContext context);

    BaseValue eval(NullNode node, Scope scope, RuntimeContext context);

    BaseValue eval(RsonNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ThisNode node, Scope scope, RuntimeContext context);

    BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context);

    BaseValue eval(LoopBlockNode node, Scope scope, RuntimeContext context);

    BaseValue eval(BlockNode node, Scope scope, RuntimeContext context);

    BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context);

    BaseValue eval(AssignNode node, Scope scope, RuntimeContext context);

    BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ForEachStatementNode node, Scope scope, RuntimeContext context);

    BaseValue eval(BreakNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context);

    BaseValue eval(CallNode node, Scope scope, RuntimeContext context);

    BaseValue eval(IndexNode node, Scope scope, RuntimeContext context);

    BaseValue eval(DotNode node, Scope scope, RuntimeContext context);

    BaseValue eval(CallChainNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ClosureDefineNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context);

    BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ImportNode node, Scope scope, RuntimeContext context);

    BaseValue eval(RuleStatementNode node, Scope scope, RuntimeContext context);

    BaseValue eval(InfixDefinitionNode node, Scope scope, RuntimeContext context);

}
