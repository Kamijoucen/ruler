package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.ast.CallAST;
import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.env.DefaultScope;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.BoolValue;
import com.kamijoucen.ruler.value.FunctionValue;
import com.kamijoucen.ruler.value.ValueType;
import com.kamijoucen.ruler.value.constant.BreakValue;
import com.kamijoucen.ruler.value.constant.ContinueValue;
import com.kamijoucen.ruler.value.constant.NoneValue;

import java.util.List;

public class DefaultStatementVisitor implements StatementVisitor {

    @Override
    public BaseValue eval(BlockAST ast, Scope scope) {

        Scope blockScope = new DefaultScope(scope);

        List<BaseAST> blocks = ast.getBlocks();

        for (BaseAST block : blocks) {
            BaseValue val = block.eval(blockScope);
            if (val.getType() == ValueType.CONTINUE) {
                break;
            } else if (val.getType() == ValueType.BREAK) {
                return val;
            }
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
            return thenBlock.eval(scope);
        } else {
            BaseAST elseBlock = ast.getElseBlock();
            if (elseBlock != null) {
                return elseBlock.eval(scope);
            }
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(AssignAST ast, Scope scope) {

        NameAST name = ast.getName();

        BaseValue expBaseValue = ast.getExpression().eval(scope);

        scope.putValue(name, expBaseValue);

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(CallAST ast, Scope scope) {

        FunctionValue function = scope.findFunction(ast.getName());

        if (function == null) {
            throw SyntaxException.withSyntax("函数未定义: " + ast.getName().name);
        }

        List<BaseAST> param = ast.getParam();

        Object[] paramVal = new BaseValue[param.size()];

        for (int i = 0; i < param.size(); i++) {
            paramVal[i] = param.get(i).eval(scope);
        }

        return (BaseValue) function.getValue().call(paramVal);
    }

    @Override
    public BaseValue eval(WhileStatementAST ast, Scope scope) {

        BaseValue conditionValue = ast.getCondition().eval(scope);

        if (conditionValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("需要一个bool类型");
        }

        BaseAST block = ast.getBlock();

        while (((BoolValue) ast.getCondition().eval(scope)).getValue()) {
            BaseValue blockValue = block.eval(scope);
            if (blockValue.getType() == ValueType.BREAK) {
                break;
            }
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(BreakAST ast, Scope scope) {
        return BreakValue.INSTANCE;
    }

    @Override
    public BaseValue eval(ContinueAST ast, Scope scope) {
        return ContinueValue.INSTANCE;
    }

    @Override
    public BaseValue eval(CallLinkedAST ast, Scope scope) {
        // a[]()[]
        // a, [], (), []

        // name = a[].name.str()[5]
        // name, =, a, [], .str, ()

        BaseValue statementValue = ast.getFirst().eval(scope);

        List<BaseAST> calls = ast.getCalls();

        DefaultScope callScope = new DefaultScope(scope);
//        callScope.putFunction();

        for (BaseAST call : calls) {
//            call.eval();
        }

        return null;
    }


}
