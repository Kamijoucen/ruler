package com.kamijoucen.ruler.common;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.AssignNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.CallNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.expression.ForEachStatementNode;
import com.kamijoucen.ruler.ast.expression.IfStatementNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.expression.IndexNode;
import com.kamijoucen.ruler.ast.expression.InfixDefinitionNode;
import com.kamijoucen.ruler.ast.expression.RuleStatementNode;
import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.expression.WhileStatementNode;
import com.kamijoucen.ruler.ast.factor.ArrayNode;
import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.ast.factor.BoolNode;
import com.kamijoucen.ruler.ast.factor.BreakNode;
import com.kamijoucen.ruler.ast.factor.ContinueNode;
import com.kamijoucen.ruler.ast.factor.DoubleNode;
import com.kamijoucen.ruler.ast.factor.IntegerNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.ast.factor.NullNode;
import com.kamijoucen.ruler.ast.factor.OutNameNode;
import com.kamijoucen.ruler.ast.factor.ReturnNode;
import com.kamijoucen.ruler.ast.factor.RsonNode;
import com.kamijoucen.ruler.ast.factor.StringNode;
import com.kamijoucen.ruler.ast.factor.ThisNode;
import com.kamijoucen.ruler.ast.factor.TypeOfNode;
import com.kamijoucen.ruler.ast.factor.UnaryOperationNode;
import com.kamijoucen.ruler.ast.factor.VirtualNode;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;

public abstract class AbstractVisitor implements NodeVisitor {

    @Override
    public BaseValue eval(NameNode node) {
        return null;
    }

    @Override
    public BaseValue eval(OutNameNode node) {
        return null;
    }

    @Override
    public BaseValue eval(IntegerNode node) {
        return null;
    }

    @Override
    public BaseValue eval(DoubleNode node) {
        return null;
    }

    @Override
    public BaseValue eval(BoolNode node) {
        return null;
    }

    @Override
    public BaseValue eval(StringNode node) {
        return null;
    }

    @Override
    public BaseValue eval(BinaryOperationNode node) {
        node.getLhs().eval(this);
        node.getRhs().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(UnaryOperationNode node) {
        node.getExp().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(ArrayNode node) {
        if (CollectionUtil.isEmpty(node.getValues())) {
            return null;
        }
        for (BaseNode arrNode : node.getValues()) {
            arrNode.eval(this);
        }
        return null;
    }

    @Override
    public BaseValue eval(NullNode node) {
        return null;
    }

    @Override
    public BaseValue eval(RsonNode node) {
        Map<String, BaseNode> properties = node.getProperties();
        if (properties == null) {
            return null;
        }
        for (BaseNode prop : properties.values()) {
            prop.eval(this);
        }
        return null;
    }

    @Override
    public BaseValue eval(ThisNode node) {
        return null;
    }

    @Override
    public BaseValue eval(TypeOfNode node) {
        node.getExp().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(BlockNode node) {
        List<BaseNode> blocks = node.getBlocks();
        if (CollectionUtil.isEmpty(blocks)) {
            return null;
        }
        for (BaseNode block : blocks) {
            block.eval(this);
        }
        return null;
    }

    @Override
    public BaseValue eval(IfStatementNode node) {
        node.getCondition().eval(this);
        node.getThenBlock().eval(this);
        node.getElseBlock().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(AssignNode node) {
        return null;
    }

    @Override
    public BaseValue eval(WhileStatementNode node) {
        node.getCondition().eval(this);
        node.getBlock().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(ForEachStatementNode node) {
        node.getList().eval(this);
        node.getBlock().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(BreakNode node) {
        return null;
    }

    @Override
    public BaseValue eval(ContinueNode node) {
        return null;
    }

    @Override
    public BaseValue eval(CallNode node) {
        return null;
    }

    @Override
    public BaseValue eval(IndexNode node) {
        return null;
    }

    @Override
    public BaseValue eval(DotNode node) {
        return null;
    }


    @Override
    public BaseValue eval(ClosureDefineNode node) {
        for (BaseNode baseNode : node.getParam()) {
            baseNode.eval(this);
        }
        node.getBlock().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(ReturnNode node) {
        for (BaseNode baseNode : node.getParam()) {
            baseNode.eval(this);
        }
        return null;
    }

    @Override
    public BaseValue eval(VariableDefineNode node) {
        BaseNode lhs = node.getLhs();
        Objects.requireNonNull(lhs);
        lhs.eval(this);
        BaseNode rhs = node.getRhs();
        if (rhs != null) {
            rhs.eval(this);
        }
        return null;
    }

    @Override
    public BaseValue eval(ImportNode node) {
        AssertUtil.TODO(null);
        return null;
    }

    @Override
    public BaseValue eval(RuleStatementNode node) {
        node.getAlias().eval(this);
        node.getBlock().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(InfixDefinitionNode node) {
        node.getFunction().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(DefaultParamValNode node) {
        node.getName().eval(this);
        node.getExp().eval(this);
        return null;
    }

    @Override
    public BaseValue eval(VirtualNode node) {
        return null;
    }

}
