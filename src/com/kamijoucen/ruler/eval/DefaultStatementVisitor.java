package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.op.CallNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.env.DefaultScope;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.OperationDefine;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.BreakValue;
import com.kamijoucen.ruler.value.constant.ContinueValue;
import com.kamijoucen.ruler.value.constant.NoneValue;

import java.util.List;

public class DefaultStatementVisitor implements StatementVisitor {

    @Override
    public BaseValue eval(BlockNode ast, Scope scope) {

        Scope blockScope = new DefaultScope(scope);

        List<BaseNode> blocks = ast.getBlocks();

        for (BaseNode block : blocks) {
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
    public BaseValue eval(IfStatementNode ast, Scope scope) {

        BaseValue conditionValue = ast.getCondition().eval(scope);

        if (conditionValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("需要一个bool类型");
        }

        BoolValue boolValue = (BoolValue) conditionValue;

        if (boolValue.getValue()) {
            BaseNode thenBlock = ast.getThenBlock();
            return thenBlock.eval(scope);
        } else {
            BaseNode elseBlock = ast.getElseBlock();
            if (elseBlock != null) {
                return elseBlock.eval(scope);
            }
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(AssignNode ast, Scope scope) {

        NameNode name = ast.getName();

        BaseValue expBaseValue = ast.getExpression().eval(scope);

        scope.putValue(name.name.name, name.isOut, expBaseValue);

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(CallNode node, Scope scope) {

        List<BaseNode> param = node.getParam();

        BaseValue[] paramVal = new BaseValue[param.size() + 1];

        paramVal[0] = node.getOperationValue();

        for (int i = 0; i < param.size(); i++) {
            paramVal[i + 1] = param.get(i).eval(scope);
        }

        return node.getOperation().compute(paramVal);

    }

    @Override
    public BaseValue eval(WhileStatementNode ast, Scope scope) {

        BaseValue conditionValue = ast.getCondition().eval(scope);

        if (conditionValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("需要一个bool类型");
        }

        BaseNode block = ast.getBlock();

        while (((BoolValue) ast.getCondition().eval(scope)).getValue()) {
            BaseValue blockValue = block.eval(scope);
            if (blockValue.getType() == ValueType.BREAK) {
                break;
            }
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(BreakNode ast, Scope scope) {
        return BreakValue.INSTANCE;
    }

    @Override
    public BaseValue eval(ContinueNode ast, Scope scope) {
        return ContinueValue.INSTANCE;
    }

    @Override
    public BaseValue eval(CallLinkedNode ast, Scope scope) {
        BaseValue statementValue = ast.getFirst().eval(scope);

        List<OperationNode> calls = ast.getCalls();

        for (OperationNode call : calls) {
            call.putOperationValue(statementValue);
            call.putOperation(OperationDefine.findOperation(call.getOperationType()));

            statementValue = call.eval(scope);
        }
        return statementValue;
    }

    @Override
    public BaseValue eval(ClosureDefineNode node, Scope scope) {

        Scope closureScope = new DefaultScope(scope);

        List<BaseNode> param = node.getParam();

        for (BaseNode p : param) {
            NameNode nameNode = (NameNode) p;
            closureScope.putValue(nameNode.name.name, false, p.eval(scope));
        }

        String funName = node.getName();

        if (funName != null) {
            scope.putValue(funName, false, new ClosureValue(closureScope, node.getBlock()));
        } else {
            return new ClosureValue(closureScope, node.getBlock());
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(IndexNode node, Scope scope) {

        return null;
    }


}
