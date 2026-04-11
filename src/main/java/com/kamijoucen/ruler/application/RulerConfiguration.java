package com.kamijoucen.ruler.application;
import com.kamijoucen.ruler.component.BinaryOperationFactory;
import com.kamijoucen.ruler.component.ParamTypePreProcess;
import com.kamijoucen.ruler.component.RuntimeBehaviorFactory;
import com.kamijoucen.ruler.component.CreateRuntimeContextFactory;
import com.kamijoucen.ruler.component.RClassManager;
import com.kamijoucen.ruler.component.IntegerNumberCache;
import com.kamijoucen.ruler.component.ValueConvertManager;
import com.kamijoucen.ruler.component.ObjectAccessControlManager;
import com.kamijoucen.ruler.component.ConfigModuleManager;
import com.kamijoucen.ruler.component.CustomImportLoaderManager;

import com.kamijoucen.ruler.domain.ast.expression.ImportNode;
import com.kamijoucen.ruler.component.NodeVisitor;
import com.kamijoucen.ruler.component.ImportCacheManager;
import com.kamijoucen.ruler.domain.type.RulerType;
import com.kamijoucen.ruler.logic.function.RulerFunction;
import com.kamijoucen.ruler.component.CallClosureExecutor;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

import java.util.List;
import java.util.Map;

public interface RulerConfiguration {

    Scope getGlobalScope();

    void registerGlobalFunction(RulerFunction function);

    void removeGlobalFunction(String functionName);

    void registerGlobalImportPathModule(String path, String alias);

    void registerGlobalImportScriptModule(String script, String alias);

    List<ImportNode> getGlobalImportModules();

    NodeVisitor<RulerType> getTypeCheckVisitor();

    ImportCacheManager getImportCache();

    BinaryOperationFactory getBinaryOperationFactory();

    NodeVisitor<BaseValue> getEvalVisitor();

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

    ConfigModuleManager getConfigModuleManager();

    CustomImportLoaderManager getCustomImportLoadManager();

}
