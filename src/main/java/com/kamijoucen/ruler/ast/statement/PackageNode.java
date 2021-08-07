package com.kamijoucen.ruler.ast.statement;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class PackageNode implements BaseNode {

    private String packageName;

    public PackageNode(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public BaseValue eval(Scope scope) {
        return null;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
