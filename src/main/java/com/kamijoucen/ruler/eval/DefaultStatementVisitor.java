package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.op.CallNode;
import com.kamijoucen.ruler.ast.op.DotNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.OperationDefine;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.BreakValue;
import com.kamijoucen.ruler.value.constant.ContinueValue;
import com.kamijoucen.ruler.value.constant.NoneValue;
import com.kamijoucen.ruler.value.constant.ReturnValue;

import java.util.ArrayList;
import java.util.List;

public class DefaultStatementVisitor implements StatementVisitor {

    @Override
    public BaseValue eval(BlockNode ast, Scope scope) {

        Scope blockScope = new Scope("block", scope);

        List<BaseNode> blocks = ast.getBlocks();

        for (BaseNode block : blocks) {
            BaseValue val = block.eval(blockScope);
            if (ValueType.CONTINUE == val.getType()) {
                break;
            } else if (ValueType.BREAK == val.getType()) {
                return val;
            } else if (ValueType.RETURN == val.getType()) {
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

        scope.update(name.name.name, expBaseValue);

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(ArrayAssignNode node, Scope scope) {

        BaseValue tempValue = node.getCalls().eval(scope);

        if (tempValue.getType() != ValueType.ARRAY) {
            throw SyntaxException.withSyntax(tempValue.getType() + "不是一个数组");
        }

        ArrayValue arrayValue = (ArrayValue) tempValue;

        BaseValue tempIndexValue = node.getIndex().getIndex().eval(scope);

        if (tempIndexValue.getType() != ValueType.INTEGER) {
            throw SyntaxException.withSyntax("数组的索引必须是数字");
        }

        IntegerValue indexValue = (IntegerValue) tempIndexValue;

        BaseValue value = node.getExpression().eval(scope);

        arrayValue.getValues().set(indexValue.getValue(), value);

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
    public BaseValue eval(DotNode node, Scope scope) {

        TokenType dotType = node.getDotType();

        BaseValue operationValue = node.getOperationValue();

        if (!(operationValue instanceof MataValue)) {
            throw SyntaxException.withSyntax(operationValue + "不支持进行'.'操作");
        }
        MataValue mataValue = (MataValue) operationValue;

        if (dotType == TokenType.IDENTIFIER) {
            return mataValue.getProperty(node.getName());
        } else if (dotType == TokenType.CALL) {
            List<BaseValue> values = new ArrayList<BaseValue>(node.getParam().size());
            for (BaseNode p : node.getParam()) {
                values.add(p.eval(scope));
            }
            return mataValue.invoke(node.getName(), values);
        }
        throw SyntaxException.withSyntax("不支持的DOT调用类型:" + dotType);
    }

    @Override
    public BaseValue eval(WhileStatementNode ast, Scope scope) {

        BaseNode block = ast.getBlock();

        while (((BoolValue) ast.getCondition().eval(scope)).getValue()) {
            BaseValue blockValue = block.eval(scope);
            if (ValueType.BREAK == blockValue.getType()) {
                break;
            } else if (ValueType.RETURN == blockValue.getType()) {
                return ReturnValue.INSTANCE;
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
            statementValue = call.eval(scope);
        }
        return statementValue;
    }

    @Override
    public BaseValue eval(ClosureDefineNode node, Scope scope) {

        List<BaseNode> param = node.getParam();

        String funName = node.getName();

        ClosureValue closureValue = new ClosureValue(scope, param, node.getBlock());

        if (funName != null) {
            scope.putLocal(funName, closureValue);
        } else {
            return closureValue;
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(ReturnNode node, Scope scope) {

        List<BaseNode> param = node.getParam();

        List<BaseValue> values = new ArrayList<BaseValue>(param.size());
        for (BaseNode baseNode : param) {
            values.add(baseNode.eval(scope));
        }

        scope.putReturnValues(values);
        return ReturnValue.INSTANCE;
    }

    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope) {

        NameNode name = node.getName();

        if (name.isOut) {
            throw SyntaxException.withSyntax("不能定义一个外部变量" + name.name.name);
        }

        BaseValue value = node.getExpression().eval(scope);

        scope.putLocal(name.name.name, value);

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(IndexNode node, Scope scope) {

        BaseValue[] computeVals = new BaseValue[2];

        computeVals[0] = node.getOperationValue();
        computeVals[1] = node.getIndex().eval(scope);

        return node.getOperation().compute(computeVals);
    }


}
