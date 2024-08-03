package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
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
    public BaseValue eval(BlockNode node) {
        symbolTable.push();
        try {
            return super.eval(node);
        } finally {
            symbolTable.pop();
        }
    }

    @Override
    public BaseValue eval(VariableDefineNode node) {
        NameNode lhs = (NameNode) node.getLhs();
        if (symbolTable.findCurrentScope(lhs.name.name) != null) {
            String message = context.getConfiguration().getMessageManager()
                    .buildMessage(MessageType.VARIABLE_REDEFINED, lhs.getLocation(), lhs.name.name);
            throw new SyntaxException(message);
        }
        this.symbolTable.define(new Symbol(lhs.name, false, null));
        return super.eval(node);
    }

    @Override
    public BaseValue eval(ClosureDefineNode node) {
        // TODO 静态粉丝阶段要分析出闭包中所有引用外部变量，运行时将这些值拷贝到闭包中
        // TODO 闭包中不允许修改外部变量
        return super.eval(node);
    }

    @Override
    public BaseValue eval(DefaultParamValNode node) {
        return super.eval(node);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

}
