package com.kamijoucen.ruler.logic.module;

import com.kamijoucen.ruler.types.module.ConfigModule;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.ImportNode;
import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.types.ast.VariableDefineNode;
import com.kamijoucen.ruler.types.ast.VirtualNode;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.module.RulerModule;
import com.kamijoucen.ruler.types.module.RulerScript;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenLocation;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.types.value.BaseValue;
import com.kamijoucen.ruler.types.value.ClosureValue;
import com.kamijoucen.ruler.types.value.FunctionValue;
import com.kamijoucen.ruler.types.value.ModuleValue;
import com.kamijoucen.ruler.types.value.NullValue;
import com.kamijoucen.ruler.logic.compiler.RulerCompiler;
import com.kamijoucen.ruler.logic.eval.RulerInterpreter;
import com.kamijoucen.ruler.types.module.ModuleState;
import com.kamijoucen.ruler.types.spi.CustomImportLoader;
import com.kamijoucen.ruler.types.spi.HostFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kamijoucen.ruler.logic.util.AssertUtil;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.logic.util.IOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ModuleLoader {

    private static final Logger log = LoggerFactory.getLogger(ModuleLoader.class);

    private ModuleLoader() {
    }

    public static BaseValue importModule(ImportNode node, Scope scope, RuntimeContext context) {
        String path = node.getPath();
        List<String> currentStack = context.getImportStack();
        if (currentStack.contains(path)) {
            throw new RulerRuntimeException(
                    "circular import: " + formatImportCycle(currentStack, path),
                    node.getLocation());
        }
        currentStack.add(path);

        try {
            return doImportModule(node, scope, context);
        } catch (RulerRuntimeException e) {
            if (isImportFailureWithChain(e) || isCircularImport(e)) {
                throw e;
            }
            throw new RulerRuntimeException(
                    "failed to import '" + path + "' (import chain: "
                            + formatImportStack(currentStack) + "): " + e.getMessage(),
                    node.getLocation(),
                    e);
        } finally {
            currentStack.remove(currentStack.size() - 1);
        }
    }

    private static BaseValue doImportModule(ImportNode node, Scope scope, RuntimeContext context) {
        String path = node.getPath();
        ModuleState modules = context.getConfiguration().getModules();
        RulerModule importModule = modules.findCached(path);
        if (importModule == null) {
            importModule = loadImportModule(path, context);
            modules.cache(path, importModule);
        }

        Scope runScope = new Scope("runtime file", false,
                new Scope("file", false, context.getGlobalScope(), null), null);
        RuntimeContext otherContext = context.createImportContext();
        RulerInterpreter.runImportModule(importModule, runScope, otherContext);

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

    private static String formatImportCycle(List<String> stack, String repeatPath) {
        int cycleStart = stack.indexOf(repeatPath);
        List<String> cycle = new ArrayList<>(stack.subList(cycleStart, stack.size()));
        cycle.add(repeatPath);
        return String.join(" -> ", cycle);
    }

    private static String formatImportStack(List<String> stack) {
        return String.join(" -> ", stack);
    }

    private static boolean isCircularImport(RulerRuntimeException e) {
        return e.getMessage() != null && e.getMessage().startsWith("circular import:");
    }

    private static boolean isImportFailureWithChain(RulerRuntimeException e) {
        return e.getMessage() != null && e.getMessage().contains("import chain:");
    }

    private static RulerModule loadImportModule(String path, RuntimeContext context) {
        ConfigModule module = context.getConfiguration().getModules().findRegistered(path);
        RulerModule importModule = null;
        String text = null;
        if (module == null || (!module.isScriptModule() && !module.isFunctionModule())) {
            text = loadScript(path, context.getConfiguration().getModules());
        } else if (module.isScriptModule()) {
            text = module.getScript();
        } else {
            importModule = createImportFunctionModule(module);
        }
        if (IOUtil.isNotBlank(text)) {
            importModule = compileScript(text, path, context);
        }
        AssertUtil.notNull(importModule, "module '" + path + "' not found");
        return importModule;
    }

    private static RulerModule compileScript(String text, String fileName, RuntimeContext context) {
        RulerScript script = new RulerScript(fileName, text);
        return RulerCompiler.compileScript(script, context.getConfiguration());
    }

    private static RulerModule createImportFunctionModule(ConfigModule module) {
        TokenLocation location = new TokenLocation(1, 0, module.getUri());
        List<BaseNode> funcDefNodes = module.getFunctions().stream().map(fun -> {
            NameNode nameNode =
                    new NameNode(new Token(TokenType.IDENTIFIER, fun.getName(), location), location);
            VirtualNode funNode = new VirtualNode(new FunctionValue(
                    new HostFunction(fun)));
            return new VariableDefineNode(nameNode, funNode, location);
        }).collect(Collectors.toList());
        RulerModule importModule = new RulerModule(module.getUri());
        importModule.setStatements(funcDefNodes);
        return importModule;
    }

    public static String loadScript(String path, ModuleState modules) {
        for (CustomImportLoader loader : modules.getLoaders()) {
            if (!loader.match(path)) {
                continue;
            }
            String text;
            try {
                text = loader.load(path);
            } catch (Exception e) {
                log.error("load custom import error, path: {}", path, e);
                continue;
            }
            return text != null ? text : IOUtil.read(path);
        }
        return IOUtil.read(path);
    }
}
