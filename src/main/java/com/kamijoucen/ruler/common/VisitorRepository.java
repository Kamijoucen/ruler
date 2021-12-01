package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.eval.DefaultStatementVisitor;
import com.kamijoucen.ruler.eval.ExpressionVisitor;
import com.kamijoucen.ruler.eval.StatementVisitor;

public class VisitorRepository {

    private static final ExpressionVisitor expressionVisitor;

    private static final StatementVisitor statementVisitor;

    static {
        expressionVisitor = new DefaultExpressionVisitor();
        statementVisitor = new DefaultStatementVisitor();
    }

    public static StatementVisitor getStatementVisitor() {
        return statementVisitor;
    }

    public static ExpressionVisitor getExpressionVisitor() {
        return expressionVisitor;
    }

}
