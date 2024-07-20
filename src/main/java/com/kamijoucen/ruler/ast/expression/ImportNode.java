package com.kamijoucen.ruler.ast.expression;

import com.kamijoucen.ruler.ast.AbstractBaseNode;
import com.kamijoucen.ruler.common.NodeVisitor;
import com.kamijoucen.ruler.token.TokenLocation;
import com.kamijoucen.ruler.value.BaseValue;

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
    public BaseValue eval(NodeVisitor visitor) {
        return visitor.eval(this);
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
