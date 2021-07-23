package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.runtime.MataData;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.*;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class DefaultExpressionVisitor implements ExpressionVisitor {

    @Override
    public BaseValue eval(NameNode node, Scope scope) {
        BaseValue baseValue = scope.find(node.name.name);
        if (baseValue == null) {
            return NullValue.INSTANCE;
        }
        return baseValue;
    }

    @Override
    public BaseValue eval(IntegerNode ast, Scope scope) {
        return new IntegerValue(ast.getValue());
    }

    @Override
    public BaseValue eval(DoubleNode ast, Scope scope) {
        return new DoubleValue(ast.getValue());
    }

    @Override
    public BaseValue eval(BoolNode ast, Scope scope) {
        return BoolValue.get(ast.getValue());
    }

    @Override
    public BaseValue eval(StringNode ast, Scope scope) {
        return new StringValue(ast.getValue());
    }

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope) {

        BaseValue val1 = node.getExp1().eval(scope);
        BaseValue val2 = node.getExp2().eval(scope);

        return node.getOperation().compute(val1, val2);
    }

    @Override
    public BaseValue eval(LogicBinaryOperationNode node, Scope scope) {

        BaseNode exp1 = node.getExp1();
        BaseNode exp2 = node.getExp2();

        return node.getLogicOperation().compute(scope, exp1, exp2);
    }

    @Override
    public BaseValue eval(ArrayNode node, Scope scope) {

        List<BaseNode> nodes = node.getValues();

        if (nodes.size() == 0) {
            return new ArrayValue(new ArrayList<BaseValue>());
        }

        List<BaseValue> values = new ArrayList<BaseValue>(nodes.size());

        for (BaseNode tempNode : nodes) {
            values.add(tempNode.eval(scope));
        }

        return new ArrayValue(values);
    }

    @Override
    public BaseValue eval(NullNode node, Scope scope) {
        return NullValue.INSTANCE;
    }

    @Override
    public BaseValue eval(RsonNode node, Scope scope) {
        MataData mataData = new MataData();
        
        for (Entry<String, BaseNode> entry : node.getProperties().entrySet()) {
            
            String name = entry.getKey();
            
            BaseValue value = entry.getValue().eval(scope);

            mataData.put(name, value);
        }
        return new RsonValue(mataData);
    }

}
