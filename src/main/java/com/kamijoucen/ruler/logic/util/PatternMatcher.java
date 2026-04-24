package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.*;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PatternMatcher {

    private PatternMatcher() {
    }

    public static Map<String, BaseValue> match(
            PatternNode pattern,
            BaseValue value,
            Scope scope,
            RuntimeContext context
    ) {
        if (pattern instanceof LiteralPatternNode) {
            BaseNode literal = ((LiteralPatternNode) pattern).getLiteral();
            BaseValue literalValue = literal.eval(scope, context);
            if (strictEqual(literalValue, value, context)) {
                return Collections.emptyMap();
            }
            return null;
        }
        if (pattern instanceof NamePatternNode) {
            String name = ((NamePatternNode) pattern).getName();
            Map<String, BaseValue> bindings = new HashMap<>();
            bindings.put(name, value);
            return bindings;
        }
        if (pattern instanceof WildcardPatternNode) {
            return Collections.emptyMap();
        }
        return null;
    }

    private static boolean strictEqual(BaseValue l, BaseValue r, RuntimeContext context) {
        ValueType lt = l.getType();
        ValueType rt = r.getType();

        if (NumberUtil.isNumber(lt) && NumberUtil.isNumber(rt)) {
            return NumberUtil.compareNumbers(l, r) == 0;
        }

        if (lt == ValueType.BOOL && rt == ValueType.BOOL) {
            return ((BoolValue) l).getValue() == ((BoolValue) r).getValue();
        }

        if (lt == ValueType.STRING && rt == ValueType.STRING) {
            return ((StringValue) l).getValue().equals(((StringValue) r).getValue());
        }

        if (lt == ValueType.NULL && rt == ValueType.NULL) {
            return true;
        }

        if (lt == ValueType.NULL || rt == ValueType.NULL) {
            return false;
        }

        if (lt != rt) {
            return false;
        }

        return l.toString().equals(r.toString());
    }

}
