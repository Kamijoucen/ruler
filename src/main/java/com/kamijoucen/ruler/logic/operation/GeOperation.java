package com.kamijoucen.ruler.logic.operation;

public class GeOperation extends AbstractCompareOperation {

    @Override
    protected boolean compareLong(long l, long r) {
        return l >= r;
    }

    @Override
    protected boolean compareDouble(double l, double r) {
        return l >= r;
    }

    @Override
    protected String operationName() {
        return "'>= '";
    }
}
