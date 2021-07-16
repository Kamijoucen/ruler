package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.ArrayList;
import java.util.List;

public class DefaultExpressionVisitor implements ExpressionVisitor {

    @Override
    public BaseValue eval(NameNode node, RuntimeContext context) {

        BaseValue value = context.findValue(node.name.name);

        if (value == null) {
            return NullValue.INSTANCE;
        }

        return value;
    }

    @Override
    public BaseValue eval(IntegerNode ast, RuntimeContext context) {
        return new IntegerValue(ast.getValue());
    }

    @Override
    public BaseValue eval(DoubleNode ast, RuntimeContext context) {
        return new DoubleValue(ast.getValue());
    }

    @Override
    public BaseValue eval(BoolNode ast, RuntimeContext context) {
        return BoolValue.get(ast.getValue());
    }

    @Override
    public BaseValue eval(StringNode ast, RuntimeContext context) {
        return new StringValue(ast.getValue());
    }

    @Override
    public BaseValue eval(BinaryOperationNode ast, RuntimeContext context) {

        BaseValue val1 = ast.getExp1().eval(context);
        BaseValue val2 = ast.getExp2().eval(context);

        return ast.getOperation().compute(val1, val2);
    }

    @Override
    public BaseValue eval(LogicBinaryOperationNode node, RuntimeContext context) {

        BaseNode exp1 = node.getExp1();
        BaseNode exp2 = node.getExp2();

        return node.getLogicOperation().compute(context, exp1, exp2);
    }

    @Override
    public BaseValue eval(ArrayNode node, RuntimeContext context) {

        List<BaseNode> nodes = node.getValues();

        if (nodes.size() == 0) {
            return new ArrayValue(new ArrayList<BaseValue>());
        }

        List<BaseValue> values = new ArrayList<BaseValue>(nodes.size());

        for (BaseNode tempNode : nodes) {
            values.add(tempNode.eval(context));
        }

        return new ArrayValue(values);
    }

    @Override
    public BaseValue eval(NullNode node, RuntimeContext context) {
        return NullValue.INSTANCE;
    }

}
