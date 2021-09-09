package com.kamijoucen.ruler.compiler;

import java.util.ArrayList;
import java.util.List;

import com.kamijoucen.ruler.ast.statement.ImportNode;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.config.RuntimeConfig;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerProgram;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.parse.DefaultLexical;
import com.kamijoucen.ruler.parse.DefaultParser;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;

public class RulerCompiler {

    private RulerProgram program;

    private RuntimeConfig config;

    private RulerScript mainScript;

    private Scope globalScope;

    public RulerCompiler(RuntimeConfig config, RulerScript mainScript, Scope globalScope) {
        this.config = config;
        this.mainScript = mainScript;
        this.globalScope = globalScope;
        this.program = new RulerProgram();
    }

    public RulerProgram compile() {
        RulerModule mainModule = compileScript(mainScript);
        program.setMainModule(mainModule);
        return program;
    }

    private RulerModule compileScript(RulerScript script) {
        RulerModule module = new RulerModule(new Scope("file", globalScope));
        // 词法分析器
        DefaultLexical lexical = new DefaultLexical(script.getContent());
        // 语法分析器
        Parser parser = new DefaultParser(lexical, module);
        // 开始解析语法
        parser.parse();

        // 解析导入语句
        List<Tuple2<String, RulerModule>> childModule
                = new ArrayList<Tuple2<String, RulerModule>>(module.getImportList().size());
        for (ImportNode node : module.getImportList()) {
            Tuple2<String, RulerModule> tuple = parseImport(node);
            childModule.add(tuple);
        }
        module.setChildModule(childModule);
        return module;
    }

    private Tuple2<String, RulerModule> parseImport(ImportNode node) {
        String absolutePath = node.getPath();
        if (IOUtil.isNotBlank(config.libPath)) {
            absolutePath = config.libPath + "/" + absolutePath;
        }
        String content = IOUtil.read(absolutePath);

        RulerModule module = compileScript(new RulerScript(null, null, content));

        return new Tuple2<String, RulerModule>(node.getAlias(), module);
    }
}
