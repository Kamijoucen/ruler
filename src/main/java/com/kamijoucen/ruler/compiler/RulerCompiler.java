package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.RulerConfiguration;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.parse.impl.DefaultLexical;
import com.kamijoucen.ruler.parse.impl.DefaultParser;
import com.kamijoucen.ruler.parse.impl.TokenStreamImpl;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.CollectionUtil;

import java.util.List;

public class RulerCompiler {

    private RulerConfiguration configuration;
    private RulerScript mainScript;

    public RulerCompiler(RulerScript mainScript, RulerConfiguration configuration) {
        this.mainScript = mainScript;
        this.configuration = configuration;
    }

    public RulerModule compileScript() {
        RulerModule mainModule = compileModule(mainScript);
        return mainModule;
    }

    public RulerModule compileExpression() {
        RulerModule module = new RulerModule(new Scope("expression", configuration.getGlobalScope()));
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
        return module;
    }

    private RulerModule compileModule(RulerScript script) {
        RulerModule module = new RulerModule(new Scope("file", configuration.getGlobalScope()));
        // 词法分析器
        DefaultLexical lexical = new DefaultLexical(script.getContent());
        // 开始词法分析
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        // 语法分析器
        Parser parser = new DefaultParser(tokenStream);
        // 开始解析语法
        List<BaseNode> statements = parser.parse();
        module.setStatements(statements);
        return module;
    }
}
