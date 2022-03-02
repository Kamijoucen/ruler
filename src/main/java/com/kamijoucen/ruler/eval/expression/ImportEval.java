package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.Init;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.ImportCache;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NoneValue;

public class ImportEval implements BaseEval<ImportNode> {
    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        String path = node.getPath();

        ImportCache importCache = context.getImportCache();
        RulerModule importModule = importCache.getImportModule(path);
        if (importModule == null) {
            String text = null;
            if (isStdImport(path)) {
                text = IOUtil.read(Init.class.getResourceAsStream(path));
            } else {
                text = IOUtil.read(path);
            }
            importModule = compilerScript(text);
            AssertUtil.notNull(importModule);
            importCache.putImportModule(path, importModule);
        }
        // todo
        Scope runScope = new Scope("runtime file", new Scope("file", null));
        runScope.initReturnSpace();
//        for (BaseNode statement : module.getStatements()) {
//            statement.eval(context, runScope);
//        }
//        ModuleValue moduleValue = new ModuleValue(module, runScope);
//        scope.defineLocal(node.getAlias(), moduleValue);
        return NoneValue.INSTANCE;
    }

    public RulerModule compilerScript(String text) {

        return null;
    }

    public boolean isStdImport(String path) {
        AssertUtil.notNull(path);
        // todo 大小写处理
        return path.startsWith("ruler/") || path.startsWith("/ruler/");
    }
}
