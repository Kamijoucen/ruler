package com.kamijoucen.ruler.runtime;

import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.module.RulerModule;

public interface RulerConfiguration {

    Scope getGlobalScope();

    void setGlobalFunction(RulerFunction function);

    void removeGlobalFunction(String functionName);

    NodeVisitor getTypeCheckVisitor();

    NodeVisitor getEvalVisitor();

    ImportCache getImportCache();

    void setGlobalModuleByPath(String path, String alias);

}
