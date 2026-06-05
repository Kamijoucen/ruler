package com.kamijoucen.ruler.logic.eval.expression;

import com.kamijoucen.ruler.domain.ast.expression.MatchCase;
import com.kamijoucen.ruler.domain.ast.expression.MatchNode;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.BoolValue;
import com.kamijoucen.ruler.domain.value.ValueType;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.logic.util.PatternMatcher;

import java.util.Map;

public class MatchEval implements BaseEval<MatchNode> {

    @Override
    public BaseValue eval(MatchNode node, Scope scope, RuntimeContext context) {
        BaseValue scrutineeValue = node.getScrutinee().eval(scope, context);

        for (MatchCase matchCase : node.getCases()) {
            Map<String, BaseValue> bindings = PatternMatcher.match(
                    matchCase.getPattern(), scrutineeValue, scope, context);
            if (bindings != null) {
                Scope caseScope = new Scope("match", false, scope, null);
                for (Map.Entry<String, BaseValue> entry : bindings.entrySet()) {
                    caseScope.defineLocal(entry.getKey(), entry.getValue());
                }

                if (matchCase.getGuard() != null) {
                    BaseValue guardValue = matchCase.getGuard().eval(caseScope, context);
                    if (guardValue.getType() != ValueType.BOOL) {
                        throw new RulerRuntimeException(
                                "guard expression must return boolean",
                                matchCase.getGuard().getLocation());
                    }
                    if (!((BoolValue) guardValue).getValue()) {
                        continue;
                    }
                }

                return matchCase.getBody().eval(caseScope, context);
            }
        }

        throw new RulerRuntimeException("match error: no case matched", node.getLocation());
    }

}
