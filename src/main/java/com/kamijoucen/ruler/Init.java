package com.kamijoucen.ruler;

import com.kamijoucen.ruler.function.*;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.FunctionValue;

public class Init {

    public static void engineInit(Scope scope) {
        registerInnerFunction(scope, new ValueConvertFunctionProxy(new PrintFunction()));
        registerInnerFunction(scope, new ValueConvertFunctionProxy(new MakeItPossibleFunction()));
        registerInnerFunction(scope, new ReturnConvertFunctionProxy(new LengthFunction()));
    }

    private static void registerInnerFunction(Scope scope, RulerFunction function) {
        FunctionValue fun = new FunctionValue(function);
        scope.putLocal(fun.getValue().getName(), fun);
    }

}
