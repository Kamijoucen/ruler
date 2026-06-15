package com.kamijoucen.ruler.logic.eval;

import java.util.List;
import com.kamijoucen.ruler.domain.ast.ImportNode;
import com.kamijoucen.ruler.domain.module.RulerModule;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public interface ImportCache {

    List<RulerModule> getAllImportModule();

    RulerModule getImportModule(String path);

    void putImportModule(String path, RulerModule module);

    BaseValue importModule(ImportNode node, Scope scope, RuntimeContext context);
}
