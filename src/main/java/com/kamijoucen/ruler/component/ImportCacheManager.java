package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.ConfigModule;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.ImportNode;
import com.kamijoucen.ruler.domain.ast.NameNode;
import com.kamijoucen.ruler.domain.ast.VariableDefineNode;
import com.kamijoucen.ruler.domain.ast.VirtualNode;
import com.kamijoucen.ruler.domain.exception.RulerRuntimeException;
import com.kamijoucen.ruler.domain.module.RulerModule;
import com.kamijoucen.ruler.domain.module.RulerScript;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.domain.value.BaseValue;
import com.kamijoucen.ruler.domain.value.ClosureValue;
import com.kamijoucen.ruler.domain.value.FunctionValue;
import com.kamijoucen.ruler.domain.value.ModuleValue;
import com.kamijoucen.ruler.domain.value.NullValue;
import com.kamijoucen.ruler.logic.eval.ImportCache;
import com.kamijoucen.ruler.logic.util.AssertUtil;
import com.kamijoucen.ruler.logic.util.CollectionUtil;
import com.kamijoucen.ruler.logic.util.IOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ImportCacheManager implements ImportCache {

    private final Map<String, RulerModule> cache = new ConcurrentHashMap<>();
    private final ThreadLocal<List<String>> importStack = ThreadLocal.withInitial(ArrayList::new);

    @Override
    public List<RulerModule> getAllImportModule() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public RulerModule getImportModule(String path) {
        if (IOUtil.isBlank(path)) {
            return null;
        }
        return cache.get(path);
    }

    @Override
    public void putImportModule(String path, RulerModule module) {
        this.cache.put(path, module);
    }

    @Override
    public BaseValue importModule(ImportNode node, Scope scope, RuntimeContext context) {
        String path = node.getPath();
        List<String> currentStack = importStack.get();
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
            if (currentStack.isEmpty()) {
                importStack.remove();
            }
        }
    }

    private BaseValue doImportModule(ImportNode node, Scope scope, RuntimeContext context) {
        String path = node.getPath();
        RulerModule importModule = getImportModule(path);
        if (importModule == null) {
            importModule = loadImportModule(path, context);
            putImportModule(path, importModule);
        }

        Scope runScope = new Scope("runtime file", false,
                new Scope("file", false, context.getGlobalScope(), null), null);
        RuntimeContext otherContext = context.getConfiguration().createDefaultRuntimeContext(null);
        RulerInterpreter interpreter =
                new RulerInterpreter(importModule, context.getConfiguration());
        interpreter.runImportModule(runScope, otherContext);

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

    private String formatImportCycle(List<String> stack, String repeatPath) {
        int cycleStart = stack.indexOf(repeatPath);
        List<String> cycle = new ArrayList<>(stack.subList(cycleStart, stack.size()));
        cycle.add(repeatPath);
        return String.join(" -> ", cycle);
    }

    private String formatImportStack(List<String> stack) {
        return String.join(" -> ", stack);
    }

    private boolean isCircularImport(RulerRuntimeException e) {
        return e.getMessage() != null && e.getMessage().startsWith("circular import:");
    }

    private boolean isImportFailureWithChain(RulerRuntimeException e) {
        return e.getMessage() != null && e.getMessage().contains("import chain:");
    }

    private RulerModule loadImportModule(String path, RuntimeContext context) {
        ConfigModule module = context.getConfiguration().getConfigModuleManager().findModule(path);
        RulerModule importModule = null;
        String text = null;
        if (module == null || (!module.isScriptModule() && !module.isFunctionModule())) {
            text = loadScript(path, context);
        } else if (module.isScriptModule()) {
            text = module.getScript();
        } else {
            importModule = createImportFunctionModule(module, context);
        }
        if (IOUtil.isNotBlank(text)) {
            importModule = compileScript(text, path, context);
        }
        AssertUtil.notNull(importModule, "module '" + path + "' not found");
        return importModule;
    }

    private RulerModule compileScript(String text, String fileName, RuntimeContext context) {
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
        if (text != null) {
            return text;
        }
        return IOUtil.read(path);
    }
}
