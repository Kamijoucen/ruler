package com.kamijoucen.ruler.domain.ast.expression;
import com.kamijoucen.ruler.domain.type.RulerType;

import com.kamijoucen.ruler.domain.ast.AbstractBaseNode;
import com.kamijoucen.ruler.domain.runtime.RuntimeContext;
import com.kamijoucen.ruler.domain.runtime.Scope;
import com.kamijoucen.ruler.domain.token.TokenLocation;
import com.kamijoucen.ruler.domain.value.BaseValue;

public class ImportNode extends AbstractBaseNode {

    private String path;
    private String alias;
    private boolean hasImportInfix;

    public ImportNode(String path, String alias, boolean hasImportInfix, TokenLocation location) {
        super(location);
        this.path = path;
        this.alias = alias;
        this.hasImportInfix = hasImportInfix;
    }

    @Override
    public BaseValue eval(Scope scope, RuntimeContext context) {
        return context.getNodeVisitor().eval(this, scope, context);
    }

    @Override
    public RulerType typeCheck(Scope scope, RuntimeContext context) {
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

    public boolean isHasImportInfix() {
        return hasImportInfix;
    }

    public void setHasImportInfix(boolean hasImportInfix) {
        this.hasImportInfix = hasImportInfix;
    }
}
