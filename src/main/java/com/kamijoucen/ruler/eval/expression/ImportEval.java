package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.ast.factor.VirtualNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.compiler.impl.RulerCompiler;
import com.kamijoucen.ruler.compiler.impl.RulerInterpreter;
import com.kamijoucen.ruler.config.impl.ImportCacheManager;
import com.kamijoucen.ruler.config.option.ConfigModule;
import com.kamijoucen.ruler.function.ValueConvertFunctionProxy;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.FunctionValue;
import com.kamijoucen.ruler.value.ModuleValue;
import com.kamijoucen.ruler.value.NullValue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImportEval implements BaseEval<ImportNode> {

    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        String path = node.getPath();

        ImportCacheManager importCache = context.getImportCache();
        RulerModule importModule = importCache.getImportModule(path);
        if (importModule == null) {
            ConfigModule module =
                    context.getConfiguration().getConfigModuleManager().findModule(path);
            String text = null;
            // 目前只会有路径导入的情况 module 可能为空
            if (module == null) {
                text = loadScript(path);
            } else {
                if (module.isScriptModule()) {
                    text = module.getScript();
                } else if (module.isFunctionModule()) {
                    List<BaseNode> funcDefNodes = module.getFunctions().stream().map(fun -> {
                        NameNode nameNode = new NameNode(
                                new Token(TokenType.IDENTIFIER, fun.getName(), null), null);
                        VirtualNode funNode = new VirtualNode(new FunctionValue(
                                new ValueConvertFunctionProxy(fun, context.getConfiguration())));
                        return new VariableDefineNode(nameNode, funNode, null);
                    }).collect(Collectors.toList());
                    importModule = new RulerModule(module.getUri());
                    importModule.setStatements(funcDefNodes);
                } else {
                    text = loadScript(path);
                }
            }
            if (IOUtil.isNotBlank(text)) {
                importModule = compilerScript(text, path, context);
            }
            AssertUtil.notNull(importModule, "module `" + path + "` not found");
            importCache.putImportModule(path, importModule);
        }
        Scope runScope = new Scope("runtime file", false,
                new Scope("file", false, context.getGlobalScope(), null), null);
        // run
        RulerInterpreter interpreter =
                new RulerInterpreter(importModule, context.getConfiguration());
        interpreter.setHasImportGlobalModule(false);
        interpreter.runScript(Collections.emptyList(), runScope);

        // include infix
        if (node.isHasImportInfix()) {
            Map<String, ClosureValue> infixOperationSpace =
                    interpreter.getRuntimeContext().getInfixOperationSpace();
            if (!CollectionUtil.isEmpty(infixOperationSpace)) {
                for (Map.Entry<String, ClosureValue> entry : infixOperationSpace.entrySet()) {
                    if (context.getInfixOperation(entry.getKey()) == null) {
                        context.addInfixOperation(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        ModuleValue moduleValue = new ModuleValue(runScope);
        scope.putLocal(node.getAlias(), moduleValue);
        return NullValue.INSTANCE;
    }

    public RulerModule compilerScript(String text, String fileName, RuntimeContext context) {
        RulerScript script = new RulerScript(fileName, text);
        return new RulerCompiler(script, context.getConfiguration()).compileScript();
    }

    private String loadScript(String path) {
        if (isStdImport(path)) {
            return IOUtil.read(Ruler.class.getResourceAsStream(path));
        } else {
            return IOUtil.read(path);
        }
    }

    private boolean isStdImport(String path) {
        AssertUtil.notNull(path);
        // todo 大小写处理
        return path.startsWith("ruler/") || path.startsWith("/ruler/");
    }

}
