package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.op.CallNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
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
    public BaseValue eval(BlockNode ast, RuntimeContext context) {
        context.push("block");

        List<BaseNode> blocks = ast.getBlocks();

        for (BaseNode block : blocks) {
            BaseValue val = block.eval(context);
            if (ValueType.CONTINUE == val.getType()) {
                break;
            } else if (ValueType.BREAK == val.getType()) {
                return val;
            } else if (ValueType.RETURN == val.getType()) {
                return val;
            }
        }
        context.pop();

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(IfStatementNode ast, RuntimeContext context) {

        BaseValue conditionValue = ast.getCondition().eval(context);

        if (conditionValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("需要一个bool类型");
        }

        BoolValue boolValue = (BoolValue) conditionValue;

        if (boolValue.getValue()) {
            BaseNode thenBlock = ast.getThenBlock();
            return thenBlock.eval(context);
        } else {
            BaseNode elseBlock = ast.getElseBlock();
            if (elseBlock != null) {
                return elseBlock.eval(context);
            }
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(AssignNode ast, RuntimeContext context) {

        NameNode name = ast.getName();

        BaseValue value = ast.getExpression().eval(context);

        context.updateValue(name.name.name, value);

        return value;
    }

    @Override
    public BaseValue eval(ArrayAssignNode node, RuntimeContext context) {

        BaseValue tempValue = node.getCalls().eval(context);

        if (tempValue.getType() != ValueType.ARRAY) {
            throw SyntaxException.withSyntax(tempValue.getType() + "不是一个数组");
        }

        ArrayValue arrayValue = (ArrayValue) tempValue;

        BaseValue tempIndexValue = node.getIndex().getIndex().eval(context);

        if (tempIndexValue.getType() != ValueType.INTEGER) {
            throw SyntaxException.withSyntax("数组的索引必须是数字");
        }

        IntegerValue indexValue = (IntegerValue) tempIndexValue;

        BaseValue value = node.getExpression().eval(context);

        arrayValue.getValues().set(indexValue.getValue(), value);

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(CallNode node, RuntimeContext context) {

        List<BaseNode> param = node.getParam();

        BaseValue[] paramVal = new BaseValue[param.size() + 1];

        paramVal[0] = node.getOperationValue();

        for (int i = 0; i < param.size(); i++) {
            paramVal[i + 1] = param.get(i).eval(context);
        }

        return node.getOperation().compute(paramVal);

    }

    @Override
    public BaseValue eval(WhileStatementNode ast, RuntimeContext context) {

        BaseNode block = ast.getBlock();

        while (((BoolValue) ast.getCondition().eval(context)).getValue()) {
            BaseValue blockValue = block.eval(context);
            if (ValueType.BREAK == blockValue.getType()) {
                break;
            } else if (ValueType.RETURN == blockValue.getType()) {
                return ReturnValue.INSTANCE;
            }
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(BreakNode ast, RuntimeContext context) {
        return BreakValue.INSTANCE;
    }

    @Override
    public BaseValue eval(ContinueNode ast, RuntimeContext context) {
        return ContinueValue.INSTANCE;
    }

    @Override
    public BaseValue eval(CallLinkedNode ast, RuntimeContext context) {
        BaseValue statementValue = ast.getFirst().eval(context);

        List<OperationNode> calls = ast.getCalls();

        for (OperationNode call : calls) {
            call.putOperationValue(statementValue);
            // todo 这里可以编译时查找
            call.putOperation(OperationDefine.findOperation(call.getOperationType()));

            statementValue = call.eval(context);
        }
        return statementValue;
    }

    @Override
    public BaseValue eval(ClosureDefineNode node, RuntimeContext context) {

        List<BaseNode> param = node.getParam();

        String funName = node.getName();

        ClosureValue closureValue = new ClosureValue(context.copyStackRef(), param, node.getBlock());

        if (funName != null) {
            context.putLocalValue(funName, closureValue);
        } else {
            return closureValue;
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(ReturnNode node, RuntimeContext context) {

        List<BaseNode> param = node.getParam();

        List<BaseValue> values = new ArrayList<BaseValue>(param.size());

        for (BaseNode baseNode : param) {
            values.add(baseNode.eval(context));
        }

        context.putReturnValues(values);

        return ReturnValue.INSTANCE;
    }

    @Override
    public BaseValue eval(VariableDefineNode node, RuntimeContext context) {

        NameNode name = node.getName();

        BaseValue value = node.getExpression().eval(context);

        context.putLocalValue(name.name.name, value);

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(IndexNode node, RuntimeContext context) {

        BaseValue[] computeVals = new BaseValue[2];

        computeVals[0] = node.getOperationValue();
        computeVals[1] = node.getIndex().eval(context);

        return node.getOperation().compute(computeVals);
    }


}
