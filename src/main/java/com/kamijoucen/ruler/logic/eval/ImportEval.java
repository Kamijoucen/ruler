package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.types.ast.ImportNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.logic.module.ModuleLoader;
import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

public class ImportEval implements BaseEval<ImportNode> {

    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        return ModuleLoader.importModule(node, scope, context);
    }
}
