package com.kamijoucen.ruler.eval.impl;

import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.ast.statement.AssignAST;
import com.kamijoucen.ruler.ast.statement.IfStatementAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.eval.StatementVisitor;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NoneValue;

public class DefaultStatementVisitor implements StatementVisitor {

    @Override
    public BaseValue eval(IfStatementAST ast, Scope scope) {
        return null;
    }

    @Override
    public BaseValue eval(AssignAST ast, Scope scope) {

        NameAST name = ast.getName();

        BaseValue expBaseValue = ast.getExpression().eval(scope);

        scope.put(name, expBaseValue);

        return NoneValue.INSTANCE;
    }


}
