package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.compiler.Parser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
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

    private final RulerConfiguration configuration;
    private final RulerScript mainScript;

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
        Parser parser = new AtomParserManager(tokenStream, configuration);
        BaseNode expression = parser.parseExpression();
        if (tokenStream.token().type != TokenType.EOF) {
            String message =
                    configuration.getMessageManager().buildMessage(MessageType.ILLEGAL_IDENTIFIER,
                            tokenStream.token().location, tokenStream.token().name);
            throw new SyntaxException(message);
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

        Parser parser = new AtomParserManager(tokenStream, configuration);
        List<BaseNode> statements = new ArrayList<>();
        while (tokenStream.token().type != TokenType.EOF) {
            if (tokenStream.token().type == TokenType.KEY_IMPORT) {
                statements.add(parser.parseImport());
            } else {
                statements.add(parser.parseStatement());
            }
        }
        module.setStatements(statements);
        return module;
    }

    private RulerModule compileModule(RulerScript script) {

        RulerModule module = new RulerModule(script.getFileName());
        DefaultLexical lexical =
                new DefaultLexical(script.getContent(), module.getFullName(), configuration);
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();
        Parser parser = new AtomParserManager(tokenStream, configuration);
        // parse import
        List<ImportNode> list = new ArrayList<>();
        while (tokenStream.token().type == TokenType.KEY_IMPORT) {
            list.add(parser.parseImport());
        }
        SyntaxCheckUtil.availableImport(list);

        List<BaseNode> rootStatements = new ArrayList<>(list);
        // parse statements
        while (tokenStream.token().type != TokenType.EOF) {
            rootStatements.add(parser.parseStatement());
        }
        module.setStatements(rootStatements);
        return module;
    }

}
