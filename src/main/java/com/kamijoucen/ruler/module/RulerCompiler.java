package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Lexical;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.util.IOUtil;

import java.util.List;

public class RulerCompiler {

    public static RulerFile compile(String content) {
        if (IOUtil.isBlank(content)) {
            return null;
        }

        Lexical lexical = new DefaultLexical(content);

        Parser parser = new DefaultParser(lexical);

        List<BaseNode> parse = parser.parse();

        return null;
    }

}
