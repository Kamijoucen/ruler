package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.config.impl.ImportCache;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.runtime.Scope;

public interface RulerConfiguration {

    Scope getGlobalScope();

    void setGlobalFunction(RulerFunction function);

    void setGlobalModuleByPath(String path, String alias);

    void removeGlobalFunction(String functionName);

    NodeVisitor getTypeCheckVisitor();

    ImportCache getImportCache();

    NodeVisitor getEvalVisitor();

    ParamTypePreProcess getParamTypePreProcess();

}
