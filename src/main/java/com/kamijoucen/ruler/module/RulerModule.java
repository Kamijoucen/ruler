package com.kamijoucen.ruler.module;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.statement.ImportNode;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.List;
import java.util.Map;

public class RulerModule {

    private List<ImportNode> importList;

    private List<BaseNode> statements;

    private Map<String, BaseValue> publicValues;

    public List<BaseNode> getStatements() {
        return statements;
    }

    public void setStatements(List<BaseNode> statements) {
        this.statements = statements;
    }

    public List<ImportNode> getImportList() {
        return importList;
    }

    public void setImportList(List<ImportNode> importList) {
        this.importList = importList;
    }

}
