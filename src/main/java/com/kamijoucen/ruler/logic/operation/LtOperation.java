package com.kamijoucen.ruler.logic.operation;

public class LtOperation extends AbstractCompareOperation {

    @Override
    protected boolean compareNumber(int cmpResult) {
        return cmpResult < 0;
    }

    @Override
    protected String operationName() {
        return "'< '";
    }
}
