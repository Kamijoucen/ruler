package com.kamijoucen.ruler.logic.compiler;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.ImportNode;
import com.kamijoucen.ruler.types.config.RulerConfiguration;
import com.kamijoucen.ruler.types.module.RulerModule;
import com.kamijoucen.ruler.types.module.RulerScript;
import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.lexer.Lexer;
import com.kamijoucen.ruler.logic.parser.Parser;
import com.kamijoucen.ruler.logic.typecheck.TypeCheckVisitor;
import com.kamijoucen.ruler.logic.util.SyntaxCheckUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RulerCompiler {
    private RulerCompiler() {}

    public static RulerModule compileScript(RulerScript script, RulerConfiguration configuration) {
        return compile(script, configuration, false);
    }

    public static RulerModule compileStatement(RulerScript script, RulerConfiguration configuration) {
        return compile(script, configuration, true);
    }

    private static RulerModule compile(RulerScript script, RulerConfiguration configuration, boolean statementMode) {
        RulerModule module = new RulerModule(statementMode ? "shell statement" : script.getFileName());
        TokenStream tokens = new TokenStream(Lexer.scan(script.getContent(), module.getFullName()));
        ParseState state = new ParseState(tokens);
        List<BaseNode> statements = new ArrayList<>();
        if (!statementMode) {
            List<ImportNode> imports = new ArrayList<>();
            while (tokens.token().type == TokenType.KEY_IMPORT) {
                imports.add(Parser.parseImport(state));
            }
            SyntaxCheckUtil.availableImport(imports);
            statements.addAll(imports);
        }
        while (tokens.token().type != TokenType.EOF) {
            if (statementMode && tokens.token().type == TokenType.KEY_IMPORT) {
                statements.add(Parser.parseImport(state));
            } else {
                statements.add(Parser.parseStatement(state));
            }
        }
        module.setStatements(statements);
        RuntimeContext context = configuration.createDefaultRuntimeContext(Collections.emptyMap());
        for (BaseNode statement : statements) {
            TypeCheckVisitor.check(statement, null, context);
        }
        return module;
    }
}
