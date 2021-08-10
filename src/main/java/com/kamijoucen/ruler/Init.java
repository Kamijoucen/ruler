package com.kamijoucen.ruler;

import com.kamijoucen.ruler.function.MakeItPossibleFunction;
import com.kamijoucen.ruler.function.PrintFunction;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.function.RulerFunctionProxy;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.FunctionValue;

public class Init {

    public static void engineInit(Scope scope) {
        registerInnerFunction(scope, new PrintFunction());
        registerInnerFunction(scope, new MakeItPossibleFunction());
    }

    private static void registerInnerFunction(Scope scope, RulerFunction function) {
        FunctionValue fun = new FunctionValue(new RulerFunctionProxy(function));
        scope.putLocal(fun.getValue().getName(), fun);
    }

}
