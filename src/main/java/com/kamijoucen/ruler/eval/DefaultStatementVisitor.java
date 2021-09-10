package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.op.CallNode;
import com.kamijoucen.ruler.ast.op.DotNode;
import com.kamijoucen.ruler.ast.NameNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.BreakValue;
import com.kamijoucen.ruler.value.constant.ContinueValue;
import com.kamijoucen.ruler.value.constant.NoneValue;
import com.kamijoucen.ruler.value.constant.ReturnValue;

import java.util.ArrayList;
import java.util.List;

public class DefaultStatementVisitor implements StatementVisitor {

    @Override
    public BaseValue eval(BlockNode ast, Scope scope, RuntimeContext context) {

        Scope blockScope = new Scope("block", scope);

        List<BaseNode> blocks = ast.getBlocks();

        for (BaseNode block : blocks) {
            BaseValue val = block.eval(context, blockScope);
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
    public BaseValue eval(IfStatementNode ast, Scope scope, RuntimeContext context) {

        BaseValue conditionValue = ast.getCondition().eval(context, scope);

        if (conditionValue.getType() != ValueType.BOOL) {
            throw SyntaxException.withSyntax("需要一个bool类型");
        }

        BoolValue boolValue = (BoolValue) conditionValue;

        if (boolValue.getValue()) {
            BaseNode thenBlock = ast.getThenBlock();
            return thenBlock.eval(context, scope);
        } else {
            BaseNode elseBlock = ast.getElseBlock();
            if (elseBlock != null) {
                return elseBlock.eval(context, scope);
            }
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context) {

        CallLinkNode leftNode = (CallLinkNode) node.getLeftNode();

        int callLength = leftNode.getCalls().size();
        if (callLength == 0) {
            NameNode name = (NameNode) leftNode.getFirst();
            BaseValue expBaseValue = node.getExpression().eval(context, scope);
            scope.update(name.name.name, expBaseValue);
            return NoneValue.INSTANCE;
        } else {
            leftNode.evalAssign(context, scope);
            OperationNode lastNode = CollectionUtil.last(leftNode.getCalls());
            assert lastNode != null;
            lastNode.assign(node.getExpression(), scope, context);
        }

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(CallNode node, Scope scope, RuntimeContext context) {

        List<BaseNode> param = node.getParam();

        BaseValue[] paramVal = new BaseValue[param.size() + 1];

        paramVal[0] = scope.getCallLinkPreviousValue();

        for (int i = 0; i < param.size(); i++) {
            paramVal[i + 1] = param.get(i).eval(context, scope);
        }

        return node.getOperation().compute(context, paramVal);
    }

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {

        TokenType dotType = node.getDotType();
        BaseValue operationValue = scope.getCallLinkPreviousValue();

        if (!(operationValue instanceof MataValue)) {
            throw SyntaxException.withSyntax(operationValue + "不支持进行'.'操作");
        }

        MataValue mataValue = (MataValue) operationValue;
        scope.putCurrentMataValue(mataValue);

        if (dotType == TokenType.IDENTIFIER) {
            return mataValue.getProperty(node.getName());
        } else if (dotType == TokenType.CALL) {
            List<BaseValue> values = new ArrayList<BaseValue>(node.getParam().size());
            for (BaseNode p : node.getParam()) {
                values.add(p.eval(context, scope));
            }
            return mataValue.invoke(context, node.getName(), values);
        } else {
            throw SyntaxException.withSyntax("不支持的DOT调用类型:" + dotType);
        }

    }

    @Override
    public BaseValue eval(WhileStatementNode ast, Scope scope, RuntimeContext context) {

        BaseNode block = ast.getBlock();

        while (((BoolValue) ast.getCondition().eval(context, scope)).getValue()) {
            BaseValue blockValue = block.eval(context, scope);
            if (ValueType.BREAK == blockValue.getType()) {
                break;
            } else if (ValueType.RETURN == blockValue.getType()) {
                return ReturnValue.INSTANCE;
            }
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(BreakNode ast, Scope scope, RuntimeContext context) {
        return BreakValue.INSTANCE;
    }

    @Override
    public BaseValue eval(ContinueNode ast, Scope scope, RuntimeContext context) {
        return ContinueValue.INSTANCE;
    }

    @Override
    public BaseValue eval(CallLinkNode node, boolean isAssign, Scope scope, RuntimeContext context) {

        BaseValue statementValue = node.getFirst().eval(context, scope);

        List<OperationNode> calls = node.getCalls();
        int length = node.getCalls().size();

        if (isAssign) {
            length--;
        }
        for (int i = 0; i < length; i++) {
            scope.putCallLinkPreviousValue(statementValue);
            statementValue = calls.get(i).eval(context, scope);
        }

        if (isAssign) {
            scope.putCallLinkPreviousValue(statementValue);
        }

        return statementValue;
    }

    @Override
    public BaseValue eval(ClosureDefineNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParam();
        String funName = node.getName();
        ClosureValue closureValue = new ClosureValue(scope, param, node.getBlock());

        if (funName != null) {
            scope.defineLocal(funName, closureValue);
        } else {
            return closureValue;
        }
        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context) {

        List<BaseNode> param = node.getParam();

        List<BaseValue> values = new ArrayList<BaseValue>(param.size());
        for (BaseNode baseNode : param) {
            values.add(baseNode.eval(context, scope));
        }

        scope.putReturnValues(values);
        return ReturnValue.INSTANCE;
    }

    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {

        BaseNode name = node.getName();

        BaseValue value = node.getExpression().eval(context, scope);

        scope.defineLocal(((NameNode) name).name.name, value);

        return NoneValue.INSTANCE;
    }

    @Override
    public BaseValue eval(IndexNode node, Scope scope, RuntimeContext context) {

        BaseValue[] computeVals = new BaseValue[2];

        computeVals[0] = scope.getCallLinkPreviousValue();
        computeVals[1] = node.getIndex().eval(context, scope);

        return node.getOperation().compute(context, computeVals);
    }

    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        RulerModule module = node.getModule();

        Scope runScope = new Scope("runtime file", module.getFileScope());

        for (BaseNode statement : module.getStatements()) {
            statement.eval(context, runScope);
        }
        ModuleValue moduleValue = new ModuleValue(module, runScope);
        scope.defineLocal(node.getAlias(), moduleValue);
        return NoneValue.INSTANCE;
    }
}
