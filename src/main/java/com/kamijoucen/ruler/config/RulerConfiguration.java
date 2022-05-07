package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.impl.ImportCache;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.Scope;

import java.util.List;

public interface RulerConfiguration {

    Scope getGlobalScope();

    void setGlobalFunction(RulerFunction function);

    void removeGlobalFunction(String functionName);

    void setGlobalImportModule(String path, String alias);

    List<ImportNode> getGlobalImportModules();

    NodeVisitor getTypeCheckVisitor();

    ImportCache getImportCache();

    NodeVisitor getEvalVisitor();

    ParamTypePreProcess getParamTypePreProcess();

    RuntimeBehaviorFactory getRuntimeBehaviorFactory();

    Integer getMaxLoopNumber();

    Integer getMaxStackDepth();

}
