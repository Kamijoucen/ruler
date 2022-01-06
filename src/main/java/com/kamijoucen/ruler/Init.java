package com.kamijoucen.ruler;

import com.kamijoucen.ruler.function.*;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.FunctionValue;

import java.io.InputStream;

public class Init {

    public static void engineInit(Scope scope) {
//        initStdlib(scope);
        registerInnerFunction(scope, new ValueConvertFunctionProxy(new PrintFunction()));
        registerInnerFunction(scope, new ValueConvertFunctionProxy(new MakeItPossibleFunction()));
        registerInnerFunction(scope, new ReturnConvertFunctionProxy(new LengthFunction()));
    }

    private static void initStdlib(Scope scope) {
        InputStream resourceAsStream = Init.class.getResourceAsStream("/ruler/std/");
        System.out.println(resourceAsStream);
    }

    private static void registerInnerFunction(Scope scope, RulerFunction function) {
        FunctionValue fun = new FunctionValue(function);
        scope.putLocal(fun.getValue().getName(), fun);
    }

}
