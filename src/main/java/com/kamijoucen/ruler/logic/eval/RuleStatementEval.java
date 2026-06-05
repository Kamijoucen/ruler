package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.expression.BlockNode;
import com.kamijoucen.ruler.domain.ast.expression.RuleStatementNode;
import com.kamijoucen.ruler.domain.ast.factor.StringNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.domain.value.SubRuleValue;

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
