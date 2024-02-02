package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.compiler.Parser;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.SyntaxCheckUtil;
import java.util.ArrayList;
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
        DefaultLexical lexical =
                new DefaultLexical(mainScript.getContent(), module.getFullName(), configuration);
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

    public RulerModule compileStatement() {
        RulerModule module = new RulerModule("shell statement");
        DefaultLexical lexical =
                new DefaultLexical(mainScript.getContent(), module.getFullName(), configuration);
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();

        Parser parser = new DefaultParser(tokenStream, configuration);
        List<BaseNode> statements = new ArrayList<>();
        while (tokenStream.token().type != TokenType.EOF) {
            statements.add(parser.parseStatement());
        }
        module.setStatements(statements);
        return module;
    }

    private RulerModule compileModule(RulerScript script) {

        final List<BaseNode> rootStatements = new ArrayList<>();

        RulerModule module = new RulerModule(script.getFileName());
        DefaultLexical lexical =
                new DefaultLexical(script.getContent(), module.getFullName(), configuration);
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();
        Parser parser = new DefaultParser(tokenStream, configuration);
        // parse import
        List<ImportNode> list = new ArrayList<>();
        while (tokenStream.token().type == TokenType.KEY_IMPORT) {
            list.add(parser.parseImport());
        }
        SyntaxCheckUtil.availableImport(list);
        rootStatements.addAll(list);
        // parse statements
        while (tokenStream.token().type != TokenType.EOF) {
            rootStatements.add(parser.parseStatement());
        }
        module.setStatements(rootStatements);
        return module;
    }

}
