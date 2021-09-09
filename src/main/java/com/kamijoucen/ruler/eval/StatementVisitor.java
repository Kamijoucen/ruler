package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.op.CallNode;
import com.kamijoucen.ruler.ast.op.DotNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface StatementVisitor {

    BaseValue eval(BlockNode node, Scope scope, RuntimeContext context);

    BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context);

    BaseValue eval(AssignNode node, Scope Scope, RuntimeContext context);

    BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context);

    BaseValue eval(BreakNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context);

    BaseValue eval(CallNode node, Scope scope, RuntimeContext context);

    BaseValue eval(IndexNode node, Scope scope, RuntimeContext context);

    BaseValue eval(DotNode node, Scope scope, RuntimeContext context);

    BaseValue eval(CallLinkNode node, boolean isAssign, Scope scope, RuntimeContext context);

    BaseValue eval(ClosureDefineNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context);

    BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context);

    BaseValue eval(ImportNode node, Scope scope, RuntimeContext context);

}
