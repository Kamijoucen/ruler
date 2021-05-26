package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.CallAST;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface StatementVisitor {

    BaseValue eval(BlockAST ast, Scope scope);

    BaseValue eval(IfStatementAST ast, Scope scope);

    BaseValue eval(AssignAST ast, Scope scope);

    BaseValue eval(CallAST ast, Scope scope);

    BaseValue eval(WhileStatementAST ast, Scope scope);

    BaseValue eval(BreakAST ast, Scope scope);

    BaseValue eval(ContinueAST ast, Scope scope);

}
