package com.kamijoucen.ruler.logic.operation;

public class GeOperation extends AbstractCompareOperation {

    @Override
    protected boolean compareNumber(int cmpResult) {
        return cmpResult >= 0;
    }

    @Override
    protected String operationName() {
        return "'>= '";
    }
}
