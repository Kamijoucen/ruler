package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.config.RuntimeConfiguration;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ModuleValue;
import com.kamijoucen.ruler.value.constant.NoneValue;

public class ImportEval implements BaseEval<ImportNode> {
    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {

        RuntimeConfiguration configuration = context.getConfiguration();

        RulerModule cachedModule = configuration.getImportModuleCache(node.getPath());
        if (cachedModule == null) {
            // todo
        }

        // todo
        Scope runScope = new Scope("runtime file", new Scope("file", null));
//        for (BaseNode statement : module.getStatements()) {
//            statement.eval(context, runScope);
//        }
//        ModuleValue moduleValue = new ModuleValue(module, runScope);
//        scope.defineLocal(node.getAlias(), moduleValue);
        return NoneValue.INSTANCE;
    }
}
