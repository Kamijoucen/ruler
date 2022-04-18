package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.ast.BaseNode;

import java.util.List;

public class RulerModule {

    private String fullName;
    private List<BaseNode> statements;

    public RulerModule(String fullName) {
        this.fullName = fullName;
    }
    public List<BaseNode> getStatements() {
        return statements;
    }
    public void setStatements(List<BaseNode> statements) {
        this.statements = statements;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
