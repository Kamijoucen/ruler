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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Ruler脚本编译器，负责将源代码编译成AST
 *
 * @author Kamijoucen
 */
public class RulerCompiler {

    private final RulerConfiguration configuration;
    private final RulerScript mainScript;

    /**
     * 创建编译器实例
     *
     * @param mainScript 要编译的脚本
     * @param configuration 配置对象
     */
    public RulerCompiler(RulerScript mainScript, RulerConfiguration configuration) {
        this.mainScript = Objects.requireNonNull(mainScript, "mainScript cannot be null");
        this.configuration = Objects.requireNonNull(configuration, "configuration cannot be null");
    }

    /**
     * 编译完整的脚本文件
     *
     * @return 编译后的模块
     */
    public RulerModule compileScript() {
        return compileModule(mainScript);
    }

    /**
     * 编译单个表达式
     *
     * @return 包含表达式的模块
     * @throws SyntaxException 如果表达式后还有其他内容
     */
    public RulerModule compileExpression() {
        RulerModule module = new RulerModule("runtime expression");
        DefaultLexical lexical = new DefaultLexical(
                mainScript.getContent(),
                module.getFullName(),
                configuration
        );
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();

        // 语法分析器
        Parser parser = new AtomParserManager(tokenStream, configuration);
        BaseNode expression = parser.parseExpression();

        // 确保表达式后没有其他内容
        if (tokenStream.token().type != TokenType.EOF) {
            String message = configuration.getMessageManager().buildMessage(
                    MessageType.ILLEGAL_IDENTIFIER,
                    tokenStream.token().location,
                    tokenStream.token().name
            );
            throw new SyntaxException(message);
        }

        module.setStatements(CollectionUtil.list(expression));
        return module;
    }

    /**
     * 编译语句序列（用于REPL环境）
     *
     * @return 包含语句序列的模块
     */
    public RulerModule compileStatement() {
        RulerModule module = new RulerModule("shell statement");
        DefaultLexical lexical = new DefaultLexical(
                mainScript.getContent(),
                module.getFullName(),
                configuration
        );
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();

        Parser parser = new AtomParserManager(tokenStream, configuration);
        List<BaseNode> statements = new ArrayList<>();

        // 解析所有语句
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

    /**
     * 编译完整的模块
     *
     * @param script 脚本对象
     * @return 编译后的模块
     */
    private RulerModule compileModule(RulerScript script) {
        RulerModule module = new RulerModule(script.getFileName());
        DefaultLexical lexical = new DefaultLexical(
                script.getContent(),
                module.getFullName(),
                configuration
        );
        TokenStreamImpl tokenStream = new TokenStreamImpl(lexical);
        tokenStream.scan();
        tokenStream.nextToken();

        Parser parser = new AtomParserManager(tokenStream, configuration);

        // 解析所有导入语句
        List<ImportNode> imports = new ArrayList<>();
        while (tokenStream.token().type == TokenType.KEY_IMPORT) {
            imports.add(parser.parseImport());
        }

        // 验证导入语句的别名是否重复
        validateImports(imports);

        // 将导入语句加入根语句列表
        List<BaseNode> rootStatements = new ArrayList<>(imports);

        // 解析其他语句
        while (tokenStream.token().type != TokenType.EOF) {
            rootStatements.add(parser.parseStatement());
        }

        module.setStatements(rootStatements);
        return module;
    }

    /**
     * 验证导入语句的别名是否有重复
     *
     * @param imports 导入语句列表
     * @throws SyntaxException 如果存在重复的别名
     */
    private void validateImports(List<ImportNode> imports) {
        Set<String> aliasSet = new HashSet<>();
        for (ImportNode node : imports) {
            String alias = node.getAlias();
            if (!aliasSet.add(alias)) {
                throw SyntaxException.withSyntax("导入别名重复: '" + alias + "'");
            }
        }
    }
}
