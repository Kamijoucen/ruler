package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.MatchCase;
import com.kamijoucen.ruler.types.ast.MatchNode;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.BoolValue;
import com.kamijoucen.ruler.types.value.ValueType;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.logic.util.PatternMatcher;

import java.util.Map;

public class MatchEval implements BaseEval<MatchNode> {

    @Override
    public BaseValue eval(MatchNode node, Scope scope, RuntimeContext context) {
        BaseValue scrutineeValue = EvalVisitor.evaluate(node.getScrutinee(), scope, context);

        for (MatchCase matchCase : node.getCases()) {
            Map<String, BaseValue> bindings = PatternMatcher.match(
                    matchCase.getPattern(), scrutineeValue, scope, context);
            if (bindings != null) {
                Scope caseScope = new Scope("match", false, scope, null);
                for (Map.Entry<String, BaseValue> entry : bindings.entrySet()) {
                    caseScope.defineLocal(entry.getKey(), entry.getValue());
                }

                if (matchCase.getGuard() != null) {
                    BaseValue guardValue = EvalVisitor.evaluate(matchCase.getGuard(), caseScope, context);
                    if (guardValue.getType() != ValueType.BOOL) {
                        throw new RulerRuntimeException(
                                "guard expression must return boolean",
                                matchCase.getGuard().getLocation());
                    }
                    if (!((BoolValue) guardValue).getValue()) {
                        continue;
                    }
                }

                return EvalVisitor.evaluate(matchCase.getBody(), caseScope, context);
            }
        }

        throw new RulerRuntimeException("match error: no case matched", node.getLocation());
    }

}
