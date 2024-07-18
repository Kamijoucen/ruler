package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.compiler.Parser;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.eval.SemanticAnalysisVisitor;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.runtime.RuntimeContext;
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
        RulerModule module = new RulerModule(mainScript.getFileName());
        DefaultLexical lexical =
                new DefaultLexical(mainScript.getContent(), module.getFullName(), configuration);
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();

        SemanticAnalysisVisitor visitor = new SemanticAnalysisVisitor();

        visitor.getSymbolTable().push();
        try {
            Parser parser = new DefaultParser(tokenStream, configuration);
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

            RuntimeContext checkContext = new RuntimeContext(configuration, module, );
            rootStatements.stream().forEach(statement -> {
                statement.eval(null, null);
            });

        } finally {
            visitor.getSymbolTable().pop();
        }
        return module;
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

        Parser parser = new DefaultParser(tokenStream, configuration);
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

}
