package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class ImportNode implements BaseNode {

    private String path;

    private String alias;

    public ImportNode(String path, String alias) {
        this.path = path;
        this.alias = alias;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
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
}
