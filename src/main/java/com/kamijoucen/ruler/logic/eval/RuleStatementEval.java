package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.BlockNode;
import com.kamijoucen.ruler.types.ast.RuleStatementNode;
import com.kamijoucen.ruler.types.ast.StringNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.types.value.SubRuleValue;

import java.util.List;

public class RuleStatementEval implements BaseEval<RuleStatementNode> {

    @Override
    public BaseValue eval(RuleStatementNode node, Scope scope, RuntimeContext context) {
        StringNode alias = node.getAlias();
        Scope ruleScope = new Scope(alias.getValue(), false, scope, null);

        BlockNode block = node.getBlock();
        List<BaseValue> returnValues = context.withIsolatedReturn(() -> {
            EvalVisitor.evaluate(block, ruleScope, context);
            return context.getReturnSpace();
        });

        if (CollectionUtil.isNotEmpty(returnValues)) {
            context.addReturnSpace(new SubRuleValue(alias.getValue(), returnValues));
        }
        return NullValue.INSTANCE;
    }
}
