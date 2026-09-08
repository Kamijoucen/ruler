package com.kamijoucen.ruler.types.ast;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.token.TokenLocation;

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
    public <T> T accept(NodeVisitor<T> visitor, Scope scope, RuntimeContext context) {
        return visitor.eval(this, scope, context);
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
