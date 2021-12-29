package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.ast.BaseNode;
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
import com.kamijoucen.ruler.typecheck.TypeCheckVisitor;
import com.kamijoucen.ruler.util.CollectionUtil;
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

    public RulerProgram compileScript() {
        RulerModule mainModule = compileModule(mainScript);
        program.setMainModule(mainModule);
        return program;
    }

    public RulerProgram compileExpression() {
        RulerModule module = new RulerModule(new Scope("expression", globalScope));
        // 词法分析器
        DefaultLexical lexical = new DefaultLexical(mainScript.getContent());
        // 开始词法分析
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();
        // 语法分析器
        DefaultParser parser = new DefaultParser(tokenStream);

        BaseNode expression = parser.parseExpression();
        module.setStatements(CollectionUtil.list(expression));
        program.setMainModule(module);
        return program;
    }

    private RulerModule compileModule(RulerScript script) {
        RulerModule module = new RulerModule(new Scope("file", globalScope));
        // 词法分析器
        DefaultLexical lexical = new DefaultLexical(script.getContent());
        // 开始词法分析
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        // 语法分析器
        Parser parser = new DefaultParser(tokenStream);
        // 开始解析语法
        List<BaseNode> statements = parser.parse();
        // 单独设置import语句
        int importSize = 0;
        for (BaseNode statement : statements) {
            if (!(statement instanceof ImportNode)) {
                break;
            }
            module.getImportList().add((ImportNode) statement);
            importSize++;
        }
        // 剔除import语句， 因为import要在编译器执行，如果不剔除这会在运行期执行
        if (importSize != 0) {
            statements = statements.subList(importSize - 1, statements.size());
        }
        module.setStatements(statements);
        // 解析导入语句
        List<Tuple2<String, RulerModule>> childModule = new ArrayList<Tuple2<String, RulerModule>>(
                module.getImportList().size());
        for (ImportNode node : module.getImportList()) {
            Tuple2<String, RulerModule> tuple = parseImport(node);
            if (tuple != null) {
                childModule.add(tuple);
            }
        }
        module.setChildModule(childModule);
        return module;
    }

    private Tuple2<String, RulerModule> parseImport(ImportNode node) {
        String absolutePath = node.getPath();
        if (IOUtil.isNotBlank(config.libPath)) {
            absolutePath = config.libPath + "/" + absolutePath;
        }
        File file = new File(absolutePath);

        String canonicalPath = null;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if (imported.contains(canonicalPath)) {
            return null;
        }
        imported.add(canonicalPath);

        String content = IOUtil.read(file);
        RulerModule module = compileModule(new RulerScript(null, null, content));
        node.setModule(module);
        return new Tuple2<String, RulerModule>(node.getAlias(), module);
    }
}
