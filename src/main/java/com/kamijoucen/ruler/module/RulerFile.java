package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.ast.BaseNode;

import java.util.List;

public class RulerFile {

    private String packageName;

    private List<String> importList;

    private List<BaseNode> statements;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImportList() {
        return importList;
    }

    public void setImportList(List<String> importList) {
        this.importList = importList;
    }

    public List<BaseNode> getStatements() {
        return statements;
    }

    public void setStatements(List<BaseNode> statements) {
        this.statements = statements;
    }

}
