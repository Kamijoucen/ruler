package com.kamijoucen.ruler.compiler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.kamijoucen.ruler.ast.statement.ImportNode;
import com.kamijoucen.ruler.config.RuntimeConfig;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerProgram;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.util.CollectionUtil;

public class RulerCompiler {



    public RulerProgram compile(RulerScript mainScript, RuntimeConfig config) {

        RulerModule mainModule = compileScript(mainScript);

        List<ImportNode> imports = mainModule.getImportList();

        return null;
    }

    private RulerModule compileScript(RulerScript script) {

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
