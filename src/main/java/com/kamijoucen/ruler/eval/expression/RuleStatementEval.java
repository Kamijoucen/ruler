package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.RuleStatementNode;
import com.kamijoucen.ruler.ast.factor.StringNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.NullValue;
import com.kamijoucen.ruler.value.SubRuleValue;

import java.util.List;

public class RuleStatementEval implements BaseEval<RuleStatementNode> {

    @Override
    public BaseValue eval(RuleStatementNode node, Scope scope, RuntimeContext context) {
        StringNode alias = node.getAlias();
        Scope ruleScope = new Scope(alias.getValue(), false, scope, null);

        BlockNode block = node.getBlock();
        block.eval(ruleScope, context);

        List<BaseValue> returnValues = context.getReturnSpace();
        context.clearReturnSpace();

        if (CollectionUtil.isNotEmpty(returnValues)) {
            context.addReturnSpace(new SubRuleValue(alias.getValue(), returnValues));
        }
        return NullValue.INSTANCE;
    }
}
