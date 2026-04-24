package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.ast.expression.*;
import com.kamijoucen.ruler.domain.ast.factor.*;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;

public interface NodeVisitor<T> {

    T eval(NameNode node, Scope scope, RuntimeContext context);

    T eval(OutNameNode node, Scope scope, RuntimeContext context);

    T eval(IntegerNode node, Scope scope, RuntimeContext context);

    T eval(DoubleNode node, Scope scope, RuntimeContext context);

    T eval(BoolNode node, Scope scope, RuntimeContext context);

    T eval(StringNode node, Scope scope, RuntimeContext context);

    T eval(StringInterpolationNode node, Scope scope, RuntimeContext context);

    T eval(BinaryOperationNode node, Scope scope, RuntimeContext context);

    T eval(UnaryOperationNode node, Scope scope, RuntimeContext context);

    T eval(ArrayNode node, Scope scope, RuntimeContext context);

    T eval(NullNode node, Scope scope, RuntimeContext context);

    T eval(RsonNode node, Scope scope, RuntimeContext context);

    T eval(TypeOfNode node, Scope scope, RuntimeContext context);

    T eval(BlockNode node, Scope scope, RuntimeContext context);

    T eval(IfStatementNode node, Scope scope, RuntimeContext context);

    T eval(AssignNode node, Scope scope, RuntimeContext context);

    T eval(WhileStatementNode node, Scope scope, RuntimeContext context);

    T eval(ForEachStatementNode node, Scope scope, RuntimeContext context);

    T eval(BreakNode node, Scope scope, RuntimeContext context);

    T eval(ContinueNode node, Scope scope, RuntimeContext context);

    T eval(CallNode node, Scope scope, RuntimeContext context);

    T eval(IndexNode node, Scope scope, RuntimeContext context);

    T eval(DotNode node, Scope scope, RuntimeContext context);

    T eval(ClosureDefineNode node, Scope scope, RuntimeContext context);

    T eval(ReturnNode node, Scope scope, RuntimeContext context);

    T eval(VariableDefineNode node, Scope scope, RuntimeContext context);

    T eval(ImportNode node, Scope scope, RuntimeContext context);

    T eval(RuleStatementNode node, Scope scope, RuntimeContext context);

    T eval(InfixDefinitionNode node, Scope scope, RuntimeContext context);

    T eval(DefaultParamValNode node, Scope scope, RuntimeContext context);

    T eval(MatchNode node, Scope scope, RuntimeContext context);

}
