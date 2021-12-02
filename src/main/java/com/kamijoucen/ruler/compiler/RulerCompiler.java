package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.config.RuntimeConfig;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerProgram;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.parse.impl.DefaultLexical;
import com.kamijoucen.ruler.parse.impl.DefaultParser;
import com.kamijoucen.ruler.parse.impl.TokenStreamImpl;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RulerCompiler {

    private RulerProgram program;

    private RuntimeConfig config;

    private RulerScript mainScript;

    private Scope globalScope;

    private Set<String> imported;

    public RulerCompiler(RuntimeConfig config, RulerScript mainScript, Scope globalScope) {
        this.config = config;
        this.mainScript = mainScript;
        this.globalScope = globalScope;
        this.program = new RulerProgram();
        this.imported = new HashSet<String>();
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
        // 开始词法分析
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        // 语法分析器
        Parser parser = new DefaultParser(module, tokenStream);
        // 开始解析语法
        parser.parse();

        // 解析导入语句
        List<Tuple2<String, RulerModule>> childModule
                = new ArrayList<Tuple2<String, RulerModule>>(module.getImportList().size());
        for (ImportNode node : module.getImportList()) {
            try {
                Tuple2<String, RulerModule> tuple = parseImport(node);
                if (tuple != null) {
                    childModule.add(tuple);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        module.setChildModule(childModule);
        return module;
    }

    private Tuple2<String, RulerModule> parseImport(ImportNode node) throws IOException {
        String absolutePath = node.getPath();
        if (IOUtil.isNotBlank(config.libPath)) {
            absolutePath = config.libPath + "/" + absolutePath;
        }
        File file = new File(absolutePath);
        String canonicalPath = file.getCanonicalPath();
        if (imported.contains(canonicalPath)) {
            return null;
        }
        imported.add(canonicalPath);

        String content = IOUtil.read(file);
        RulerModule module = compileScript(new RulerScript(null, null, content));
        node.setModule(module);
        return new Tuple2<String, RulerModule>(node.getAlias(), module);
    }
}
