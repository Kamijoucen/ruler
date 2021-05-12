package com.kamijoucen.ruler;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.env.GlobalScope;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Lexical;
import com.kamijoucen.ruler.parse.Parser;

import java.util.List;

public class Ruler {

    private static final GlobalScope globalScope = new GlobalScope();

    static {



    }

    public static RuleScript compile(String text) {

        Lexical lexical = new DefaultLexical(text);

        Parser parser = new DefaultParser(lexical);

        List<BaseAST> asts = parser.parse();

        return new RuleScript(globalScope, asts);
    }

}
