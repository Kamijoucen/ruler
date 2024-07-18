package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.common.AbstractVisitor;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.compiler.symbol.Symbol;
import com.kamijoucen.ruler.compiler.symbol.SymbolTable;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class SemanticAnalysisVisitor extends AbstractVisitor {

    private final SymbolTable symbolTable;

    public SemanticAnalysisVisitor() {
        this.symbolTable = new SymbolTable();
    }

    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        symbolTable.push();
        try {
            return super.eval(node, scope, context);
        } finally {
            symbolTable.pop();
        }
    }

    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        NameNode lhs = (NameNode) node.getLhs();
        if (symbolTable.findCurrentScope(lhs.name.name) != null) {
            String message = context.getConfiguration().getMessageManager()
                    .buildMessage(MessageType.VARIABLE_REDEFINED, lhs.getLocation(), lhs.name.name);
            throw new SyntaxException(message);
        }
        this.symbolTable.define(new Symbol(lhs.name, false, null));
        return super.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(DefaultParamValNode node, Scope scope, RuntimeContext context) {
        return super.eval(node, scope, context);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

}
