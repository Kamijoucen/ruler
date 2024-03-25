package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.impl.ImportCacheManager;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.CallClosureExecutor;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;
import java.util.Map;

public interface RulerConfiguration {

    Scope getGlobalScope();

    void registerGlobalFunction(RulerFunction function);

    void removeGlobalFunction(String functionName);

    @Deprecated
    void registerGlobalImportPathModule(String path, String alias);

    @Deprecated
    void registerGlobalImportScriptModule(String script, String alias);

    List<ImportNode> getGlobalImportModules();

    NodeVisitor getTypeCheckVisitor();

    ImportCacheManager getImportCache();

    BinaryOperationFactory getBinaryOperationFactory();

    NodeVisitor getEvalVisitor();

    ParamTypePreProcess getParamTypePreProcess();

    RuntimeBehaviorFactory getRuntimeBehaviorFactory();

    CreateRuntimeContextFactory getCreateDefaultRuntimeContextFactory();

    RClassManager getRClassManager();

    Integer getMaxLoopNumber();

    Integer getMaxStackDepth();

    RuntimeContext createDefaultRuntimeContext(Map<String, BaseValue> outSpace);

    IntegerNumberCache getIntegerNumberCache();

    ValueConvertManager getValueConvertManager();

    CallClosureExecutor getCallClosureExecutor();

    ObjectAccessControlManager getObjectAccessControlManager();

    MessageManager getMessageManager();

    ConfigModuleManager getConfigModuleManager();

}
