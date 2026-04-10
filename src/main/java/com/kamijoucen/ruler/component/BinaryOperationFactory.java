package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.logic.operation.BinaryOperation;

public interface BinaryOperationFactory {

    BinaryOperation findOperation(String type);

    void registerOperation(String type, BinaryOperation operation);

}
