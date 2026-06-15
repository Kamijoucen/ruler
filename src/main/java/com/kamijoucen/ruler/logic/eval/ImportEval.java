package com.kamijoucen.ruler.logic.eval;

import com.kamijoucen.ruler.domain.ast.ImportNode;
import com.kamijoucen.ruler.logic.BaseEval;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class ImportEval implements BaseEval<ImportNode> {

    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        return context.getImportCache().importModule(node, scope, context);
    }
}
