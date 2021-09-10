package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.MataData;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.runtime.TypeMapping;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class DefaultExpressionVisitor implements ExpressionVisitor {

    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        BaseValue baseValue = scope.find(node.name.name);
        if (baseValue == null) {
            throw SyntaxException.withSyntax("'" + node.name.name + "'未定义");
        }
        return baseValue;
    }

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        BaseValue value = context.findOutValue(node.name.name);
        if (value == null) {
            return NullValue.INSTANCE;
        }
        return value;
    }

    @Override
    public BaseValue eval(IntegerNode ast, Scope scope, RuntimeContext context) {
        return new IntegerValue(ast.getValue());
    }

    @Override
    public BaseValue eval(DoubleNode ast, Scope scope, RuntimeContext context) {
        return new DoubleValue(ast.getValue());
    }

    @Override
    public BaseValue eval(BoolNode ast, Scope scope, RuntimeContext context) {
        return BoolValue.get(ast.getValue());
    }

    @Override
    public BaseValue eval(StringNode ast, Scope scope, RuntimeContext context) {
        return new StringValue(ast.getValue());
    }

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {

        BaseValue val1 = node.getExp1().eval(context, scope);
        BaseValue val2 = node.getExp2().eval(context, scope);

        return node.getOperation().compute(context, val1, val2);
    }

    @Override
    public BaseValue eval(LogicBinaryOperationNode node, Scope scope, RuntimeContext context) {

        BaseNode exp1 = node.getExp1();
        BaseNode exp2 = node.getExp2();

        return node.getLogicOperation().compute(context, scope, exp1, exp2);
    }

    @Override
    public BaseValue eval(ArrayNode node, Scope scope, RuntimeContext context) {

        List<BaseNode> nodes = node.getValues();

        if (nodes.size() == 0) {
            return new ArrayValue(new ArrayList<BaseValue>());
        }

        List<BaseValue> values = new ArrayList<BaseValue>(nodes.size());

        for (BaseNode tempNode : nodes) {
            values.add(tempNode.eval(context, scope));
        }

        return new ArrayValue(values);
    }

    @Override
    public BaseValue eval(NullNode node, Scope scope, RuntimeContext context) {
        return NullValue.INSTANCE;
    }

    @Override
    public BaseValue eval(RsonNode node, Scope scope, RuntimeContext context) {
        MataData mataData = new MataData();

        for (Entry<String, BaseNode> entry : node.getProperties().entrySet()) {

            String name = entry.getKey();

            BaseValue value = entry.getValue().eval(context, scope);

            mataData.put(name, value);
        }
        return new RsonValue(mataData);
    }

    @Override
    public BaseValue eval(ThisNode node, Scope scope, RuntimeContext context) {
        MataValue mataValue = scope.getCurrentContextMataValue();
        AssertUtil.notNull(mataValue);
        return mataValue;
    }

    @Override
    public BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        BaseValue value = node.getExp().eval(context, scope);
        String type = TypeMapping.lookUp(value.getType());
        if (IOUtil.isBlank(type)) {
            throw SyntaxException.withSyntax("typeof 不支持的表达式");
        }
        return new StringValue(type);
    }
}
