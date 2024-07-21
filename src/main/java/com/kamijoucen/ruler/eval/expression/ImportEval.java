package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.ast.factor.VirtualNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.compiler.impl.RulerCompiler;
import com.kamijoucen.ruler.compiler.impl.RulerInterpreter;
import com.kamijoucen.ruler.config.impl.ImportCacheManager;
import com.kamijoucen.ruler.config.option.ConfigModule;
import com.kamijoucen.ruler.function.ValueConvertFunctionProxy;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.runtime.Environment;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ImportEval implements BaseEval<ImportNode> {

    @Override
    public BaseValue eval(ImportNode node, Environment env, RuntimeContext context, NodeVisitor visitor) {
        String path = node.getPath();

        ImportCacheManager importCache = context.getImportCache();
        RulerModule importModule = importCache.getImportModule(path);
        if (importModule == null) {
            importModule = loadImportModule(path, context);
            importCache.putImportModule(path, importModule);
        }
        Scope runScope = new Scope("runtime file", false,
                new Scope("file", false, context.getGlobalScope(), null), null);
        // run
        RuntimeContext otherContext = context.getConfiguration().createDefaultRuntimeContext(null);
        RulerInterpreter interpreter =
                new RulerInterpreter(importModule, context.getConfiguration());
        interpreter.setHasImportGlobalModule(false);
        interpreter.runScript(runScope, otherContext);
        // include infix
        if (node.isHasImportInfix()) {
            Map<String, ClosureValue> infixOperationSpace = otherContext.getInfixOperationSpace();
            if (!CollectionUtil.isEmpty(infixOperationSpace)) {
                for (Map.Entry<String, ClosureValue> entry : infixOperationSpace.entrySet()) {
                    if (context.getInfixOperation(entry.getKey()) == null) {
                        context.addInfixOperation(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        scope.putLocal(node.getAlias(), new ModuleValue(runScope));
        return NullValue.INSTANCE;
    }

    private RulerModule loadImportModule(String path, RuntimeContext context) {
        ConfigModule module = context.getConfiguration().getConfigModuleManager().findModule(path);
        RulerModule importModule = null;
        String text = null;
        // 目前只会有路径导入的情况 module 可能为空
        if (module == null) {
            text = loadScript(path, context);
        } else {
            if (module.isScriptModule()) {
                text = module.getScript();
            } else if (module.isFunctionModule()) {
                importModule = createImportFunctionModule(module, context);
            } else {
                text = loadScript(path, context);
            }
        }
        if (IOUtil.isNotBlank(text)) {
            importModule = compilerScript(text, path, context);
        }
        AssertUtil.notNull(importModule, "module '" + path + "' not found");
        return importModule;
    }

    private RulerModule compilerScript(String text, String fileName, RuntimeContext context) {
        RulerScript script = new RulerScript(fileName, text);
        return new RulerCompiler(script, context.getConfiguration()).compileScript();
    }

    private RulerModule createImportFunctionModule(ConfigModule module, RuntimeContext context) {
        List<BaseNode> funcDefNodes = module.getFunctions().stream().map(fun -> {
            NameNode nameNode =
                    new NameNode(new Token(TokenType.IDENTIFIER, fun.getName(), null), null);
            VirtualNode funNode = new VirtualNode(new FunctionValue(
                    new ValueConvertFunctionProxy(fun, context.getConfiguration())));
            return new VariableDefineNode(nameNode, funNode, null);
        }).collect(Collectors.toList());
        RulerModule importModule = new RulerModule(module.getUri());
        importModule.setStatements(funcDefNodes);
        return importModule;
    }

    private String loadScript(String path, RuntimeContext context) {
        String text = context.getConfiguration().getCustomImportLoadManager().load(path);
        if (!Objects.isNull(text)) {
            return text;
        }
        return IOUtil.read(path);
    }

}
