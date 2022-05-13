package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.*;
import com.kamijoucen.ruler.ast.facotr.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;
import java.util.Map;

public class AbstractVisitor implements NodeVisitor {
    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(DoubleNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(BoolNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(StringNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        node.getExp1().eval(context, scope);
        node.getExp2().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(LogicBinaryOperationNode node, Scope scope, RuntimeContext context) {
        node.getExp1().eval(context, scope);
        node.getExp2().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        node.getExp().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(ArrayNode node, Scope scope, RuntimeContext context) {
        if (CollectionUtil.isEmpty(node.getValues())) {
            return null;
        }
        for (BaseNode arrNode : node.getValues()) {
            arrNode.eval(context, scope);
        }
        return null;
    }

    @Override
    public BaseValue eval(NullNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(RsonNode node, Scope scope, RuntimeContext context) {
        Map<String, BaseNode> properties = node.getProperties();
        if (properties == null) {
            return null;
        }
        for (BaseNode prop : properties.values()) {
            prop.eval(context, scope);
        }
        return null;
    }

    @Override
    public BaseValue eval(ThisNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        node.getExp().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(LoopBlockNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> blocks = node.getBlocks();
        if (CollectionUtil.isEmpty(blocks)) {
            return null;
        }
        for (BaseNode block : blocks) {
            block.eval(context, scope);
        }
        return null;
    }

    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> blocks = node.getBlocks();
        if (CollectionUtil.isEmpty(blocks)) {
            return null;
        }
        for (BaseNode block : blocks) {
            block.eval(context, scope);
        }
        return null;
    }

    @Override
    public BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        node.getCondition().eval(context, scope);
        node.getThenBlock().eval(context, scope);
        node.getElseBlock().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context) {
        node.getLeftNode().eval(context, scope);
        node.getExpression().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        node.getCondition().eval(context, scope);
        node.getBlock().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        node.getList().eval(context, scope);
        node.getBlock().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(BreakNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context) {
        return null;
    }

    @Override
    public BaseValue eval(CallNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParam();
        if (CollectionUtil.isEmpty(param)) {
            return null;
        }
        for (BaseNode baseNode : param) {
            baseNode.eval(context, scope);
        }
        return null;
    }

    @Override
    public BaseValue eval(IndexNode node, Scope scope, RuntimeContext context) {
        node.getIndex().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        List<BaseNode> param = node.getParam();
        if (CollectionUtil.isEmpty(param)) {
            return null;
        }
        for (BaseNode baseNode : param) {
            baseNode.eval(context, scope);
        }
        return null;
    }

    @Override
    public BaseValue eval(CallLinkNode node, Scope scope, RuntimeContext context) {
        node.getFirst().eval(context, scope);
        List<OperationNode> calls = node.getCalls();
        for (BaseNode baseNode : calls) {
            baseNode.eval(context, scope);
        }
        return null;
    }

    @Override
    public BaseValue eval(ClosureDefineNode node, Scope scope, RuntimeContext context) {
        for (BaseNode baseNode : node.getParam()) {
            baseNode.eval(context, scope);
        }
        node.getBlock().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context) {
        for (BaseNode baseNode : node.getParam()) {
            baseNode.eval(context, scope);
        }
        return null;
    }

    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        node.getName().eval(context, scope);
        node.getExpression().eval(context, scope);
        return null;
    }

    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        AssertUtil.todo(null);
        return null;
    }

    @Override
    public BaseValue eval(RuleStatementNode node, Scope scope, RuntimeContext context) {
        node.getAlias().eval(context, scope);
        node.getBlock().eval(context, scope);
        return null;
    }

}
