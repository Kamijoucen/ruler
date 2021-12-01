package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.statement.ImportNode;
import com.kamijoucen.ruler.eval.BaseEval;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ModuleValue;
import com.kamijoucen.ruler.value.constant.NoneValue;

public class ImportEval implements BaseEval<ImportNode> {
    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        RulerModule module = node.getModule();
        Scope runScope = new Scope("runtime file", module.getFileScope());
        for (BaseNode statement : module.getStatements()) {
            statement.eval(context, runScope);
        }
        ModuleValue moduleValue = new ModuleValue(module, runScope);
        scope.defineLocal(node.getAlias(), moduleValue);
        return NoneValue.INSTANCE;
    }
}
