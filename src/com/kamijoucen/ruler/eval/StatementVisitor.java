package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.statement.AssignAST;
import com.kamijoucen.ruler.ast.statement.IfStatementAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public interface StatementVisitor {

    BaseValue eval(IfStatementAST ast, Scope scope);

    BaseValue eval(AssignAST ast, Scope scope);


}
