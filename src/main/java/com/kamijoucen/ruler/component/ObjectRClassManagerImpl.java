package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.logic.function.*;
import com.kamijoucen.ruler.logic.function.array.*;
import com.kamijoucen.ruler.logic.function.object.*;
import com.kamijoucen.ruler.logic.function.classinfo.LengthFunction;
import com.kamijoucen.ruler.logic.function.classinfo.PrintSelfFunction;
import com.kamijoucen.ruler.logic.function.classinfo.PushFunction;
import com.kamijoucen.ruler.logic.function.classinfo.ToStringFunction;
import com.kamijoucen.ruler.logic.function.string.*;
import com.kamijoucen.ruler.logic.util.AssertUtil;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.FunctionValue;
import com.kamijoucen.ruler.domain.value.RClass;
import com.kamijoucen.ruler.domain.value.RClassValue;
import com.kamijoucen.ruler.domain.value.ValueType;

import java.util.HashMap;
import java.util.Map;

public class ObjectRClassManagerImpl implements RClassManager {

    public final Map<ValueType, RClass> RCLASS_MAP = new HashMap<>();

    public ObjectRClassManagerImpl() {
        RCLASS_MAP.put(ValueType.STRING, createStringClass());
        RCLASS_MAP.put(ValueType.ARRAY, createArrayClass());
        RCLASS_MAP.put(ValueType.INTEGER, createIntegerClass());
        RCLASS_MAP.put(ValueType.RSON, createRsonClass());
    }

    private RClass createBaseRClass() {
        RClass rClass = new RClassValue();
        addFunToRClass(new ToStringFunction(), rClass);
        addFunToRClass(new PrintSelfFunction(), rClass);
        return rClass;
    }

    private RClass createRsonClass() {
        RClass baseRClass = createBaseRClass();
        addFunToRClass("get", new ObjectGetFunction(), baseRClass);
        addFunToRClass("pick", new ObjectPickFunction(), baseRClass);
        addFunToRClass("omit", new ObjectOmitFunction(), baseRClass);
        return baseRClass;
    }

    private RClass createStringClass() {
        RClass baseRClass = createBaseRClass();
        addFunToRClass(new LengthFunction(), baseRClass);
        addFunToRClass("substring", new SubstringFunction(), baseRClass);
        addFunToRClass("indexOf", new StringIndexOfFunction(), baseRClass);
        addFunToRClass("replace", new ReplaceFunction(), baseRClass);
        addFunToRClass("split", new SplitFunction(), baseRClass);
        addFunToRClass("trim", new TrimFunction(), baseRClass);
        addFunToRClass("upperCase", new UpperCaseFunction(), baseRClass);
        addFunToRClass("lowerCase", new LowerCaseFunction(), baseRClass);
        addFunToRClass("startsWith", new StartsWithFunction(), baseRClass);
        addFunToRClass("endsWith", new EndsWithFunction(), baseRClass);
        addFunToRClass("charAt", new CharAtFunction(), baseRClass);
        addFunToRClass("array", new ToArrayFunction(), baseRClass);
        return baseRClass;
    }

    private RClass createArrayClass() {
        RClass rClass = createBaseRClass();
        addFunToRClass(new LengthFunction(), rClass);
        addFunToRClass(new PushFunction(), rClass);
        addFunToRClass("pop", new PopFunction(), rClass);
        addFunToRClass("shift", new ShiftFunction(), rClass);
        addFunToRClass("unshift", new UnshiftFunction(), rClass);
        addFunToRClass("slice", new SliceFunction(), rClass);
        addFunToRClass("reverse", new ReverseFunction(), rClass);
        addFunToRClass("concat", new ConcatFunction(), rClass);
        addFunToRClass("join", new JoinFunction(), rClass);
        addFunToRClass("indexOf", new ArrayIndexOfFunction(), rClass);
        addFunToRClass("sort", new SortFunction(), rClass);
        addFunToRClass("map", new MapFunction(), rClass);
        addFunToRClass("filter", new FilterFunction(), rClass);
        addFunToRClass("reduce", new ReduceFunction(), rClass);
        addFunToRClass("find", new FindFunction(), rClass);
        addFunToRClass("findIndex", new FindIndexFunction(), rClass);
        addFunToRClass("isEmpty", new ArrayIsEmptyFunction(), rClass);
        addFunToRClass("first", new ArrayFirstFunction(), rClass);
        addFunToRClass("last", new ArrayLastFunction(), rClass);
        addFunToRClass("contains", new ArrayContainsFunction(), rClass);
        return rClass;
    }

    private RClass createIntegerClass() {
        RClass rClass = createBaseRClass();
        return rClass;
    }

    @Override
    public RClass getClassValue(ValueType valueType) {
        RClass value = RCLASS_MAP.get(valueType);
        AssertUtil.notNull(value);
        return value;
    }

    private void addFunToRClass(RulerFunction func, RClass rClass) {
        rClass.getProperties().put(func.getName(), new FunctionValue(func));
    }

    private void addFunToRClass(String name, RulerFunction func, RClass rClass) {
        rClass.getProperties().put(name, new FunctionValue(new NamedWrapper(name, func)));
    }

    private static class NamedWrapper implements RulerFunction {
        private final String name;
        private final RulerFunction delegate;

        NamedWrapper(String name, RulerFunction delegate) {
            this.name = name;
            this.delegate = delegate;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
            return delegate.call(context, currentScope, self, param);
        }
    }
}
