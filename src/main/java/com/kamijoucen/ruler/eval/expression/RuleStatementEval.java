package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.RuleStatementNode;
import com.kamijoucen.ruler.ast.facotr.StringNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.SubRuleValue;
import com.kamijoucen.ruler.value.constant.NullValue;

import java.util.List;

public class RuleStatementEval implements BaseEval<RuleStatementNode> {

    @Override
    public BaseValue eval(RuleStatementNode node, Scope scope, RuntimeContext context) {
        StringNode alias = node.getAlias();
        Scope ruleScope = new Scope(alias.getValue(), scope);
        ruleScope.initReturnSpace();

        BlockNode block = node.getBlock();
        block.eval(ruleScope, context);

        List<BaseValue> returnValues = ruleScope.getReturnSpace();
        if (CollectionUtil.isNotEmpty(returnValues)) {
            scope.putReturnValues(
                    CollectionUtil.list((BaseValue) new SubRuleValue(alias.getValue(), returnValues)));
        }
        return NullValue.INSTANCE;
    }
}
