package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.common.Tuple2;
import com.kamijoucen.ruler.runtime.Scope;

import java.util.ArrayList;
import java.util.List;

public class RulerModule {

    private String fullName;
    private List<BaseNode> statements;
    private Scope fileScope;

    public RulerModule(Scope fileScope) {
        this.fileScope = fileScope;
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
    public Scope getFileScope() {
        return fileScope;
    }
    public void setFileScope(Scope fileScope) {
        this.fileScope = fileScope;
    }
}
