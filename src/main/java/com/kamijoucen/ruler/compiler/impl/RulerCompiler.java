package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.compiler.Parser;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.token.TokenType;
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
        RulerModule module = new RulerModule("runtime expression");
        // 词法分析器
        DefaultLexical lexical = new DefaultLexical(mainScript.getContent(), module.getFullName());
        // 开始词法分析
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();
        // 语法分析器
        DefaultParser parser = new DefaultParser(tokenStream, configuration);
        BaseNode expression = parser.parseExpression();
        if (tokenStream.token().type != TokenType.EOF) {
            throw SyntaxException.withSyntax("错误的表达式结构：" + tokenStream.token());
        }
        module.setStatements(CollectionUtil.list(expression));
        return module;
    }

    private RulerModule compileModule(RulerScript script) {
        RulerModule module = new RulerModule(script.getFileName());
        // 词法分析器
        DefaultLexical lexical = new DefaultLexical(script.getContent(), module.getFullName());
        // 开始词法分析
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        // 语法分析器
        Parser parser = new DefaultParser(tokenStream, configuration);
        // 开始解析语法
        List<BaseNode> statements = parser.parse();
        module.setStatements(statements);
        return module;
    }
}
