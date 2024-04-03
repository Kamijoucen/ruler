package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.operation.BinaryOperation;

public interface BinaryOperationFactory {

    BinaryOperation findOperation(String type);

    void registerOperation(String type, BinaryOperation operation);

}
