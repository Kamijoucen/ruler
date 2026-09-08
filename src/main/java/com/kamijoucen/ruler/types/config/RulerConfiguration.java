package com.kamijoucen.ruler.types.config;

import com.kamijoucen.ruler.logic.module.StdImportLoader;
import com.kamijoucen.ruler.logic.spi.ConfigurationHooks;
import com.kamijoucen.ruler.logic.util.IOUtil;
import com.kamijoucen.ruler.stdlib.Builtins;
import com.kamijoucen.ruler.types.ast.ImportNode;
import com.kamijoucen.ruler.types.exception.RulerRuntimeException;
import com.kamijoucen.ruler.types.module.ConfigModule;
import com.kamijoucen.ruler.types.module.ModuleState;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.spi.HostFunction;
import com.kamijoucen.ruler.types.spi.RulerFunction;
import com.kamijoucen.ruler.types.value.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * One engine's shared environment. Configure it before executing scripts;
 * create separate instances when globals or modules must be isolated.
 */
public final class RulerConfiguration {
    private final Scope globalScope = new Scope("root", false, null, null);
    private final List<ImportNode> globalImports = new ArrayList<>();
    private final ModuleState modules = new ModuleState();
    private final Map<ValueType, RClass> classes = Builtins.createClasses();
    private int maxLoopNumber = -1;
    private int maxStackDepth = -1;

    public RulerConfiguration() {
        Builtins.install(this);
        modules.registerLoader(new StdImportLoader());
        ConfigurationHooks.load(this);
    }

    public Scope getGlobalScope() { return globalScope; }
    public ModuleState getModules() { return modules; }

    public RClass getClassValue(ValueType type) {
        RClass value = type == null ? null : classes.get(type);
        if (value == null) {
            throw new RulerRuntimeException("value is null");
        }
        return value;
    }

    public void registerGlobalFunction(RulerFunction function) {
        globalScope.putLocal(function.getName(), new FunctionValue(new HostFunction(function)));
    }

    public void removeGlobalFunction(String name) { globalScope.remove(name); }

    public void registerGlobalImportPathModule(String path, String alias) {
        globalImports.add(new ImportNode(path, alias, false, null));
    }

    public void registerGlobalImportScriptModule(String script, String alias) {
        String path = IOUtil.getVirtualPath(script, alias);
        modules.register(ConfigModule.createScriptModule(path, script));
        registerGlobalImportPathModule(path, alias);
    }

    public List<ImportNode> getGlobalImportModules() { return new ArrayList<>(globalImports); }
    public int getMaxLoopNumber() { return maxLoopNumber; }
    public void setMaxLoopNumber(int limit) { maxLoopNumber = limit; }
    public int getMaxStackDepth() { return maxStackDepth; }
    public void setMaxStackDepth(int limit) { maxStackDepth = limit; }

    public RuntimeContext createDefaultRuntimeContext(Map<String, BaseValue> outSpace) {
        RuntimeContext context = new RuntimeContext(this);
        context.setOutSpace(outSpace);
        return context;
    }
}
