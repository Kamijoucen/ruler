package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.op.CallNode;
import com.kamijoucen.ruler.ast.op.DotNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface StatementVisitor {

    BaseValue eval(BlockNode node, Scope scope);

    BaseValue eval(IfStatementNode node, Scope scope);

    BaseValue eval(AssignNode node, Scope Scope);

    BaseValue eval(WhileStatementNode node, Scope scope);

    BaseValue eval(BreakNode node, Scope scope);

    BaseValue eval(ContinueNode node, Scope scope);

    BaseValue eval(CallNode node, Scope scope);

    BaseValue eval(IndexNode node, Scope scope);

    BaseValue eval(DotNode node, Scope scope);

    BaseValue eval(CallLinkNode node, boolean isAssign, Scope scope);

    BaseValue eval(ClosureDefineNode node, Scope scope);

    BaseValue eval(ReturnNode node, Scope scope);

    BaseValue eval(VariableDefineNode node, Scope scope);

}
