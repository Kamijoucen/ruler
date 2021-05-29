package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.env.DefaultScope;
import com.kamijoucen.ruler.env.Scope;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Lexical;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.runtime.RulerFunctionProxy;

import java.util.List;

public class Ruler {

    private static final Scope globalScope = new DefaultScope(null, true, true);

    static {
        Init.engineInit();
    }

    public static RuleScript compile(String text) {

        Lexical lexical = new DefaultLexical(text);

        Parser parser = new DefaultParser(lexical);

        List<BaseNode> asts = parser.parse();

        return new RuleScript(globalScope, asts);
    }


    public static void registerFunction(RulerFunction function) {
        Ruler.globalScope.putFunction(new RulerFunctionProxy(function), true);
    }

    static void registerInnerFunction(RulerFunction function) {
        Ruler.globalScope.putFunction(new RulerFunctionProxy(function), false);
    }
}
