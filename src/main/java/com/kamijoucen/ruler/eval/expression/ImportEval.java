package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.compiler.impl.RulerCompiler;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.config.impl.ImportCache;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ModuleValue;
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
                text = IOUtil.read(Ruler.class.getResourceAsStream(path));
            } else {
                text = IOUtil.read(path);
            }
            importModule = compilerScript(text, context);
            AssertUtil.notNull(importModule);

            importCache.putImportModule(path, importModule);
        }
        Scope runScope = new Scope("runtime file", new Scope("file", context.getGlobalScope()));
        runScope.initReturnSpace();

        for (BaseNode statement : importModule.getStatements()) {
            statement.eval(context, runScope);
        }
        ModuleValue moduleValue = new ModuleValue(importModule, runScope);
        scope.defineLocal(node.getAlias(), moduleValue);
        return NoneValue.INSTANCE;
    }

    public RulerModule compilerScript(String text, RuntimeContext context) {
        RulerScript script = new RulerScript(null, null, text);
        return new RulerCompiler(script, context.getConfiguration()).compileScript();
    }

    public boolean isStdImport(String path) {
        AssertUtil.notNull(path);
        // todo 大小写处理
        return path.startsWith("ruler/") || path.startsWith("/ruler/");
    }
}
