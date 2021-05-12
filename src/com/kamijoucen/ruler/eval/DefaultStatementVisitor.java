package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.ast.statement.AssignAST;
import com.kamijoucen.ruler.ast.statement.BlockAST;
import com.kamijoucen.ruler.ast.statement.IfStatementAST;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.NoneValue;

import java.util.List;

public class DefaultStatementVisitor implements StatementVisitor {

    @Override
    public BaseValue eval(BlockAST ast, Scope scope) {

        Scope blockScope = new Scope(scope);

        List<BaseAST> blocks = ast.getBlocks();

        for (BaseAST block : blocks) {
            block.eval(blockScope);
        }

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(IfStatementAST ast, Scope scope) {

        BaseValue conditionValue = ast.getCondition().eval(scope);

        if (conditionValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("需要一个bool类型");
        }

        BoolValue boolValue = (BoolValue) conditionValue;

        if (boolValue.getValue()) {
            BaseAST thenBlock = ast.getThenBlock();
            thenBlock.eval(scope);
        } else {
            BaseAST elseBlock = ast.getElseBlock();
            elseBlock.eval(scope);
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(AssignAST ast, Scope scope) {

        NameAST name = ast.getName();

        BaseValue expBaseValue = ast.getExpression().eval(scope);

        scope.put(name, expBaseValue);

        return NoneValue.INSTANCE;
    }


}
