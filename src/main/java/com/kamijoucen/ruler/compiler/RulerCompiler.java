package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Parser;

public class RulerCompiler {


    public RulerModule compile(RulerScript script) {

        RulerModule module = new RulerModule();
        // 词法分析器
        DefaultLexical lexical = new DefaultLexical(script.getContent());
        // 语法分析器
        Parser parser = new DefaultParser(lexical, module);
        // 开始解析语法
        parser.parse();

        return module;
    }


}
