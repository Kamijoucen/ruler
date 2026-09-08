package com.kamijoucen.ruler.logic;

import com.kamijoucen.ruler.types.ast.NodeVisitor;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.*;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.logic.util.AssertUtil;
import com.kamijoucen.ruler.logic.util.CollectionUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractVisitor<T> implements NodeVisitor<T> {
    @Override
    public T eval(VirtualNode node, Scope scope, RuntimeContext context) {
        return null;
    }

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
            eval(part, scope, context);
        }
        return null;
    }

    @Override
    public T eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        eval(node.getLhs(), scope, context);
        eval(node.getRhs(), scope, context);
        return null;
    }

    @Override
    public T eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        eval(node.getExp(), scope, context);
        return null;
    }

    @Override
    public T eval(ArrayNode node, Scope scope, RuntimeContext context) {
        if (CollectionUtil.isEmpty(node.getValues())) {
            return null;
        }
        for (BaseNode arrNode : node.getValues()) {
            eval(arrNode, scope, context);
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
            eval(prop, scope, context);
        }
        return null;
    }

    @Override
    public T eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        eval(node.getExp(), scope, context);
        return null;
    }

    @Override
    public T eval(BlockNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> blocks = node.getBlocks();
        if (CollectionUtil.isEmpty(blocks)) {
            return null;
        }
        for (BaseNode block : blocks) {
            eval(block, scope, context);
        }
        return null;
    }

    @Override
    public T eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        eval(node.getCondition(), scope, context);
        eval(node.getThenBlock(), scope, context);
        eval(node.getElseBlock(), scope, context);
        return null;
    }

    @Override
    public T eval(AssignNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public T eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        eval(node.getCondition(), scope, context);
        eval(node.getBlock(), scope, context);
        return null;
    }

    @Override
    public T eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        eval(node.getList(), scope, context);
        eval(node.getBlock(), scope, context);
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
            eval(baseNode, scope, context);
        }
        eval(node.getBlock(), scope, context);
        return null;
    }

    @Override
    public T eval(ReturnNode node, Scope scope, RuntimeContext context) {
        for (BaseNode baseNode : node.getParam()) {
            eval(baseNode, scope, context);
        }
        return null;
    }

    @Override
    public T eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        BaseNode lhs = node.getLhs();
        Objects.requireNonNull(lhs);
        eval(lhs, scope, context);
        BaseNode rhs = node.getRhs();
        if (rhs != null) {
            eval(rhs, scope, context);
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
        eval(node.getAlias(), scope, context);
        eval(node.getBlock(), scope, context);
        return null;
    }

    @Override
    public T eval(InfixDefinitionNode node, Scope scope, RuntimeContext context) {
        eval(node.getFunction(), scope, context);
        return null;
    }

    @Override
    public T eval(DefaultParamValNode node, Scope scope, RuntimeContext context) {
        eval(node.getName(), scope, context);
        eval(node.getExp(), scope, context);
        return null;
    }

    @Override
    public T eval(MatchNode node, Scope scope, RuntimeContext context) {
        eval(node.getScrutinee(), scope, context);
        for (MatchCase matchCase : node.getCases()) {
            eval(matchCase.getBody(), scope, context);
        }
        return null;
    }

}
