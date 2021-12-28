package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class ImportNode extends AbstractBaseNode {

    private String path;

    private String alias;

    private RulerModule module;

    public ImportNode(String path, String alias) {
        this.path = path;
        this.alias = alias;
    }

    @Override
    public BaseValue eval(RuntimeContext context, Scope scope) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public BaseValue typeCheck(RuntimeContext context, Scope scope) {
        return context.getTypeCheckVisitor().eval(this, scope, context);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public RulerModule getModule() {
        return module;
    }

    public void setModule(RulerModule module) {
        this.module = module;
    }
}
