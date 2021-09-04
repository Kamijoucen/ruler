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

    private RulerProgram program;

    private RuntimeConfig config;

    private RulerScript mainScript;

    public RulerCompiler(RuntimeConfig config, RulerScript mainScript) {
        this.config = config;
        this.mainScript = mainScript;
        this.program = new RulerProgram();
    }

    public RulerProgram compile() {
        RulerModule mainModule = compileScript(mainScript);

        program.setMainModule(mainModule);
        // program.getModules().put(mainModule.getFullName(), mainModule);

        return program;
    }

    private RulerModule compileScript(RulerScript script) {
        RulerModule module = new RulerModule();
        // 词法分析器
        DefaultLexical lexical = new DefaultLexical(script.getContent());
        // 语法分析器
        Parser parser = new DefaultParser(lexical, module);
        // 开始解析语法
        parser.parse();

        // 解析导入语句
        for (ImportNode node : module.getImportList()) {
            parseImport(node);
        }
        return module;
    }

    private void parseImport(ImportNode node) {

    }

}
