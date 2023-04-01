package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.impl.ImportCache;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;
import java.util.Map;

public interface RulerConfiguration {

    Scope getGlobalScope();

    void setGlobalFunction(RulerFunction function);

    void removeGlobalFunction(String functionName);

    void setGlobalImportModule(String path, String alias);

    void setGlobalImportScriptModule(String alias, String script);

    List<ImportNode> getGlobalImportModules();

    NodeVisitor getTypeCheckVisitor();

    ImportCache getImportCache();

    NodeVisitor getEvalVisitor();

    ParamTypePreProcess getParamTypePreProcess();

    RuntimeBehaviorFactory getRuntimeBehaviorFactory();

    CreateRuntimeContextFactory getCreateDefaultRuntimeContextFactory();

    RClassFactory getRClassFactory();

    Integer getMaxLoopNumber();

    Integer getMaxStackDepth();

    RuntimeContext createDefaultRuntimeContext(Map<String, BaseValue> outSpace);

    IntegerNumberCache getIntegerNumberCache();

}
