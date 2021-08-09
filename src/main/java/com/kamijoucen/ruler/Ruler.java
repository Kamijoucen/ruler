package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Lexical;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.function.RulerFunctionProxy;
import com.kamijoucen.ruler.value.FunctionValue;

import java.util.List;

public class Ruler {

    private static final Scope globalScope = new Scope("root", null);

    static {
        Init.engineInit();
    }

    public static RuleScript compile(String text) {

        Lexical lexical = new DefaultLexical(text);

        Parser parser = new DefaultParser(lexical, null);

        List<BaseNode> asts = parser.parse();

        return new RuleScript(new Scope("file", globalScope), asts);
    }


    public static void registerFunction(RulerFunction function) {
        FunctionValue fun = new FunctionValue(new RulerFunctionProxy(function));
        Ruler.globalScope.putLocal(fun.getValue().getName(), fun);
    }

    static void registerInnerFunction(RulerFunction function) {
        FunctionValue fun = new FunctionValue(new RulerFunctionProxy(function));
        Ruler.globalScope.putLocal(fun.getValue().getName(), fun);
    }
}
