package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.operation.BinaryOperation;

public interface BinaryOperationFactory {

    BinaryOperation findOperation(String type);

    void putOperation(String type, BinaryOperation operation);

}
