package com.kamijoucen.ruler.eval.expression;

import com.kamijoucen.ruler.Ruler;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.expression.ImportScriptNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.compiler.impl.RulerCompiler;
import com.kamijoucen.ruler.compiler.impl.RulerInterpreter;
import com.kamijoucen.ruler.config.impl.ImportCache;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.module.RulerScript;
import com.kamijoucen.ruler.parameter.RulerParameter;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ClosureValue;
import com.kamijoucen.ruler.value.ModuleValue;
import com.kamijoucen.ruler.value.NullValue;

import java.util.Collections;
import java.util.Map;

public class ImportEval implements BaseEval<ImportNode> {

    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        String path = node.getPath();

        ImportCache importCache = context.getImportCache();
        RulerModule importModule = importCache.getImportModule(path);
        if (importModule == null) {
            String text = null;
            if (node instanceof ImportScriptNode) {
                ImportScriptNode scriptNode = (ImportScriptNode) node;
                text = scriptNode.getScript();
            } else {
                if (isStdImport(path)) {
                    text = IOUtil.read(Ruler.class.getResourceAsStream(path));
                } else {
                    text = IOUtil.read(path);
                }
            }
            importModule = compilerScript(text, path, context);
            AssertUtil.notNull(importModule);

            importCache.putImportModule(path, importModule);
        }
        Scope runScope = new Scope("runtime file", false, new Scope("file", false, context.getGlobalScope(), null),
                null);
        // run
        RulerInterpreter interpreter = new RulerInterpreter(importModule, context.getConfiguration());
        interpreter.setHasImportGlobalModule(false);
        interpreter.runScript(Collections.<RulerParameter>emptyList(), runScope);

        // include infix
        if (node.isHasImportInfix()) {
            Map<String, ClosureValue> infixOperationSpace = interpreter.getRuntimeContext().getInfixOperationSpace();
            if (!CollectionUtil.isEmpty(infixOperationSpace)) {
                for (Map.Entry<String, ClosureValue> entry : infixOperationSpace.entrySet()) {
                    if (context.getInfixOperation(entry.getKey()) == null) {
                        context.addInfixOperation(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        ModuleValue moduleValue = new ModuleValue(runScope);
        scope.putLocal(node.getAlias(), moduleValue);
        return NullValue.INSTANCE;
    }

    public RulerModule compilerScript(String text, String fileName, RuntimeContext context) {
        RulerScript script = new RulerScript(fileName, text);
        return new RulerCompiler(script, context.getConfiguration()).compileScript();
    }

    public boolean isStdImport(String path) {
        AssertUtil.notNull(path);
        // todo 大小写处理
        return path.startsWith("ruler/") || path.startsWith("/ruler/");
    }
}
