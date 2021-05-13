package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.env.GlobalScope;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Lexical;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.runtime.RulerFunction;
import com.kamijoucen.ruler.runtime.RulerFunctionProxy;
import com.kamijoucen.ruler.runtime.function.MakeItPossibleFunction;
import com.kamijoucen.ruler.runtime.function.PrintFunction;

import java.util.List;

public class Ruler {

    private static final GlobalScope globalScope = new GlobalScope();

    static {
        engineInit();
    }

    public static void registerFunction(RulerFunction function) {
        Ruler.globalScope.putFunction(new RulerFunctionProxy(function));
    }

    public static RuleScript compile(String text) {

        Lexical lexical = new DefaultLexical(text);

        Parser parser = new DefaultParser(lexical);

        List<BaseAST> asts = parser.parse();

        return new RuleScript(globalScope, asts);
    }


    private static void engineInit() {
        Ruler.registerFunction(new PrintFunction());

        Ruler.registerFunction(new MakeItPossibleFunction());
    }


}
