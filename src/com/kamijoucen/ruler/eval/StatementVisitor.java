package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.op.CallNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface StatementVisitor {

    BaseValue eval(BlockNode node, RuntimeContext context);

    BaseValue eval(IfStatementNode node, RuntimeContext context);

    BaseValue eval(AssignNode node, RuntimeContext context);

    BaseValue eval(ArrayAssignNode node, RuntimeContext context);

    BaseValue eval(WhileStatementNode node, RuntimeContext context);

    BaseValue eval(BreakNode node, RuntimeContext context);

    BaseValue eval(ContinueNode node, RuntimeContext context);

    BaseValue eval(CallNode node, RuntimeContext context);

    BaseValue eval(IndexNode node, RuntimeContext context);

    BaseValue eval(CallLinkedNode node, RuntimeContext context);

    BaseValue eval(ClosureDefineNode node, RuntimeContext context);

    BaseValue eval(ReturnNode node, RuntimeContext context);

    BaseValue eval(VariableDefineNode node, RuntimeContext context);

}
