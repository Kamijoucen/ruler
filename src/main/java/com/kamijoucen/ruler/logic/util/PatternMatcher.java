package com.kamijoucen.ruler.logic.util;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.*;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.*;

import java.util.*;

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
            BaseValue literalValue;
            if (literal instanceof NameNode) {
                // bare 标识符值比较：从 scope 中查找变量值
                literalValue = scope.find(((NameNode) literal).name.name);
                if (literalValue == null) {
                    throw new RulerRuntimeException("undefined variable in pattern: " + ((NameNode) literal).name.name);
                }
            } else {
                literalValue = literal.eval(scope, context);
            }
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
        if (pattern instanceof ArrayPatternNode) {
            return matchArrayPattern((ArrayPatternNode) pattern, value, scope, context);
        }
        if (pattern instanceof ObjectPatternNode) {
            return matchObjectPattern((ObjectPatternNode) pattern, value, scope, context);
        }
        if (pattern instanceof RestPatternNode) {
            RestPatternNode rest = (RestPatternNode) pattern;
            if (rest.getName() == null || "_".equals(rest.getName())) {
                return Collections.emptyMap();
            }
            Map<String, BaseValue> bindings = new HashMap<>();
            bindings.put(rest.getName(), value);
            return bindings;
        }
        return null;
    }

    private static Map<String, BaseValue> matchArrayPattern(
            ArrayPatternNode pattern,
            BaseValue value,
            Scope scope,
            RuntimeContext context
    ) {
        if (value.getType() != ValueType.ARRAY) {
            return null;
        }
        List<BaseValue> values = ((ArrayValue) value).getValues();
        List<PatternNode> elements = pattern.getElements();
        RestPatternNode restPattern = pattern.getRestPattern();

        int minLen = elements.size();
        boolean hasRest = restPattern != null;

        if (!hasRest && values.size() != minLen) {
            return null;
        }
        if (hasRest && values.size() < minLen) {
            return null;
        }

        Map<String, BaseValue> bindings = new HashMap<>();
        for (int i = 0; i < minLen; i++) {
            Map<String, BaseValue> b = match(elements.get(i), values.get(i), scope, context);
            if (b == null) {
                return null;
            }
            mergeBindings(bindings, b);
        }

        if (hasRest) {
            List<BaseValue> restValues = new ArrayList<>(values.subList(minLen, values.size()));
            ArrayValue restArray = new ArrayValue(restValues);
            Map<String, BaseValue> b = match(restPattern, restArray, scope, context);
            if (b == null) {
                return null;
            }
            mergeBindings(bindings, b);
        }

        return bindings;
    }

    private static Map<String, BaseValue> matchObjectPattern(
            ObjectPatternNode pattern,
            BaseValue value,
            Scope scope,
            RuntimeContext context
    ) {
        if (!(value instanceof RsonValue)) {
            return null;
        }
        Map<String, BaseValue> fields;
        try {
            fields = ((RsonValue) value).getFields();
        } catch (UnsupportedOperationException e) {
            return null;
        }

        List<ObjectPatternField> objFields = pattern.getFields();
        RestPatternNode restPattern = pattern.getRestPattern();

        Map<String, BaseValue> bindings = new HashMap<>();
        for (ObjectPatternField field : objFields) {
            BaseValue fieldValue = fields.get(field.getFieldName());
            if (fieldValue == null) {
                return null;
            }
            Map<String, BaseValue> b = match(field.getPattern(), fieldValue, scope, context);
            if (b == null) {
                return null;
            }
            mergeBindings(bindings, b);
        }

        if (restPattern != null) {
            Map<String, BaseValue> restFields = new HashMap<>(fields);
            for (ObjectPatternField field : objFields) {
                restFields.remove(field.getFieldName());
            }
            RsonValue restObject = new RsonValue(restFields);
            Map<String, BaseValue> b = match(restPattern, restObject, scope, context);
            if (b == null) {
                return null;
            }
            mergeBindings(bindings, b);
        }

        return bindings;
    }

    private static void mergeBindings(Map<String, BaseValue> target, Map<String, BaseValue> source) {
        for (Map.Entry<String, BaseValue> entry : source.entrySet()) {
            putBinding(target, entry.getKey(), entry.getValue());
        }
    }

    private static void putBinding(Map<String, BaseValue> bindings, String name, BaseValue value) {
        if (bindings.containsKey(name)) {
            throw new RulerRuntimeException("duplicate binding in pattern: " + name);
        }
        bindings.put(name, value);
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
        return false;
    }

}
