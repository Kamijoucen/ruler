package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.*;
import com.kamijoucen.ruler.domain.ast.factor.*;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.logic.util.AssertUtil;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractVisitor<T> implements NodeVisitor<T> {
    @Override
    public T eval(NameNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(OutNameNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(DoubleNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(BoolNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(StringNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(StringInterpolationNode node, Scope scope, RuntimeContext context) {
        if (CollectionUtil.isEmpty(node.getParts())) {
            return null;
        }
        for (BaseNode part : node.getParts()) {
            part.eval(scope, context);
        }
        return null;
    }

    @Override
    public T eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        node.getLhs().eval(scope, context);
        node.getRhs().eval(scope, context);
        return null;
    }

    @Override
    public T eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        node.getExp().eval(scope, context);
        return null;
    }

    @Override
    public T eval(ArrayNode node, Scope scope, RuntimeContext context) {
        if (CollectionUtil.isEmpty(node.getValues())) {
            return null;
        }
        for (BaseNode arrNode : node.getValues()) {
            arrNode.eval(scope, context);
        }
        return null;
    }

    @Override
    public T eval(NullNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(RsonNode node, Scope scope, RuntimeContext context) {
        Map<String, BaseNode> properties = node.getProperties();
        if (properties == null) {
            return null;
        }
        for (BaseNode prop : properties.values()) {
            prop.eval(scope, context);
        }
        return null;
    }

    @Override
    public T eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        node.getExp().eval(scope, context);
        return null;
    }

    @Override
    public T eval(BlockNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> blocks = node.getBlocks();
        if (CollectionUtil.isEmpty(blocks)) {
            return null;
        }
        for (BaseNode block : blocks) {
            block.eval(scope, context);
        }
        return null;
    }

    @Override
    public T eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        node.getCondition().eval(scope, context);
        node.getThenBlock().eval(scope, context);
        node.getElseBlock().eval(scope, context);
        return null;
    }

    @Override
    public T eval(AssignNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        node.getCondition().eval(scope, context);
        node.getBlock().eval(scope, context);
        return null;
    }

    @Override
    public T eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        node.getList().eval(scope, context);
        node.getBlock().eval(scope, context);
        return null;
    }

    @Override
    public T eval(BreakNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(ContinueNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(CallNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(IndexNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(DotNode node, Scope scope, RuntimeContext context) {
        return null;
    }


    @Override
    public T eval(ClosureDefineNode node, Scope scope, RuntimeContext context) {
        for (BaseNode baseNode : node.getParam()) {
            baseNode.eval(scope, context);
        }
        node.getBlock().eval(scope, context);
        return null;
    }

    @Override
    public T eval(ReturnNode node, Scope scope, RuntimeContext context) {
        for (BaseNode baseNode : node.getParam()) {
            baseNode.eval(scope, context);
        }
        return null;
    }

    @Override
    public T eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        BaseNode lhs = node.getLhs();
        Objects.requireNonNull(lhs);
        lhs.eval(scope, context);
        BaseNode rhs = node.getRhs();
        if (rhs != null) {
            rhs.eval(scope, context);
        }
        return null;
    }

    @Override
    public T eval(ImportNode node, Scope scope, RuntimeContext context) {
        AssertUtil.TODO(null);
        return null;
    }

    @Override
    public T eval(RuleStatementNode node, Scope scope, RuntimeContext context) {
        node.getAlias().eval(scope, context);
        node.getBlock().eval(scope, context);
        return null;
    }

    @Override
    public T eval(InfixDefinitionNode node, Scope scope, RuntimeContext context) {
        node.getFunction().eval(scope, context);
        return null;
    }

    @Override
    public T eval(DefaultParamValNode node, Scope scope, RuntimeContext context) {
        node.getName().eval(scope, context);
        node.getExp().eval(scope, context);
        return null;
    }

}
