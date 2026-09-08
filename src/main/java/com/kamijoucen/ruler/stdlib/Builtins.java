package com.kamijoucen.ruler.stdlib;

import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.spi.NamedFunction;
import com.kamijoucen.ruler.types.value.*;
import com.kamijoucen.ruler.stdlib.array.*;
import com.kamijoucen.ruler.stdlib.object.*;
import com.kamijoucen.ruler.stdlib.string.*;
import com.kamijoucen.ruler.stdlib.classinfo.*;
import java.util.Map;

import com.kamijoucen.ruler.stdlib.CallFunction;
import com.kamijoucen.ruler.stdlib.CharAtFunction;
import com.kamijoucen.ruler.stdlib.DatetimeFunction;
import com.kamijoucen.ruler.stdlib.MakeItPossibleFunction;
import com.kamijoucen.ruler.stdlib.PanicFunction;
import com.kamijoucen.ruler.stdlib.PrintFunction;
import com.kamijoucen.ruler.stdlib.ProxyFunction;
import com.kamijoucen.ruler.stdlib.TimestampFunction;
import com.kamijoucen.ruler.stdlib.ToBooleanFunction;
import com.kamijoucen.ruler.stdlib.ToNumberFunction;
import com.kamijoucen.ruler.stdlib.math.*;
import com.kamijoucen.ruler.stdlib.net.HttpRequestFunction;
import com.kamijoucen.ruler.stdlib.net.HttpSendFunction;
import com.kamijoucen.ruler.stdlib.type.*;

/** Installs the language's fixed builtins into an engine's own environment. */
public final class Builtins {
    private Builtins() {}

    public static void install(RulerConfiguration configuration) {
        configuration.registerGlobalFunction(new PrintFunction());
        configuration.registerGlobalFunction(new MakeItPossibleFunction());
        configuration.registerGlobalFunction(new DatetimeFunction());
        configuration.registerGlobalFunction(new TimestampFunction());
        configuration.registerGlobalFunction(new PanicFunction());

        putGlobal(configuration, new ToNumberFunction());
        putGlobal(configuration, new ToBooleanFunction());
        putGlobal(configuration, new ProxyFunction());
        putGlobal(configuration, new CallFunction());
        putGlobal(configuration, new CharAtFunction());

        putGlobal(configuration, new AbsFunction());
        putGlobal(configuration, new MinFunction());
        putGlobal(configuration, new MaxFunction());
        putGlobal(configuration, new RoundFunction());
        putGlobal(configuration, new FloorFunction());
        putGlobal(configuration, new CeilFunction());
        putGlobal(configuration, new PowFunction());
        putGlobal(configuration, new SqrtFunction());
        putGlobal(configuration, new RandomFunction());

        putGlobal(configuration, new SubstringFunction());
        putGlobal(configuration, new StringIndexOfFunction());
        putGlobal(configuration, new ReplaceFunction());
        putGlobal(configuration, new SplitFunction());
        putGlobal(configuration, new TrimFunction());
        putGlobal(configuration, new UpperCaseFunction());
        putGlobal(configuration, new LowerCaseFunction());
        putGlobal(configuration, new StartsWithFunction());
        putGlobal(configuration, new EndsWithFunction());

        putGlobal(configuration, new MapFunction());
        putGlobal(configuration, new FilterFunction());
        putGlobal(configuration, new ReduceFunction());
        putGlobal(configuration, new FindFunction());
        putGlobal(configuration, new FindIndexFunction());
        putGlobal(configuration, new SliceFunction());
        putGlobal(configuration, new ConcatFunction());
        putGlobal(configuration, new JoinFunction());
        putGlobal(configuration, new ReverseFunction());
        putGlobal(configuration, new PopFunction());
        putGlobal(configuration, new ShiftFunction());
        putGlobal(configuration, new UnshiftFunction());
        putGlobal(configuration, new ArrayIndexOfFunction());
        putGlobal(configuration, new SortFunction());

        putGlobal(configuration, new KeysFunction());
        putGlobal(configuration, new ValuesFunction());
        putGlobal(configuration, new HasKeyFunction());
        putGlobal(configuration, new MergeFunction());

        putGlobal(configuration, new IsNullFunction());
        putGlobal(configuration, new IsNumberFunction());
        putGlobal(configuration, new IsStringFunction());
        putGlobal(configuration, new IsBoolFunction());
        putGlobal(configuration, new IsArrayFunction());
        putGlobal(configuration, new IsFunctionFunction());
        putGlobal(configuration, new IsDateFunction());

        configuration.registerGlobalFunction(new HttpRequestFunction());
        configuration.registerGlobalFunction(new HttpSendFunction());
    }

    private static void putGlobal(RulerConfiguration configuration, RulerFunction function) {
        configuration.getGlobalScope().putLocal(function.getName(), new FunctionValue(function));
    }

    public static Map<ValueType, RClass> createClasses() {
        return Map.of(ValueType.STRING, createStringClass(), ValueType.ARRAY, createArrayClass(),
                ValueType.INTEGER, createIntegerClass(), ValueType.RSON, createRsonClass());
    }

    private static RClass createBaseRClass() {
        RClass rClass = new RClassValue();
        addFunToRClass(new ToStringFunction(), rClass);
        addFunToRClass(new PrintSelfFunction(), rClass);
        return rClass;
    }

    private static RClass createRsonClass() {
        RClass baseRClass = createBaseRClass();
        addFunToRClass("get", new ObjectGetFunction(), baseRClass);
        addFunToRClass("pick", new ObjectPickFunction(), baseRClass);
        addFunToRClass("omit", new ObjectOmitFunction(), baseRClass);
        return baseRClass;
    }

    private static RClass createStringClass() {
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

    private static RClass createArrayClass() {
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

    private static RClass createIntegerClass() {
        RClass rClass = createBaseRClass();
        return rClass;
    }

    private static void addFunToRClass(RulerFunction func, RClass rClass) {
        rClass.getProperties().put(func.getName(), new FunctionValue(func));
    }

    private static void addFunToRClass(String name, RulerFunction func, RClass rClass) {
        rClass.getProperties().put(name, new FunctionValue(new NamedFunction(name, func)));
    }

}
