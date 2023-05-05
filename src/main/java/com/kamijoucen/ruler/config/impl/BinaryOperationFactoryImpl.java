package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.BinaryOperationFactory;
import com.kamijoucen.ruler.operation.*;
import com.kamijoucen.ruler.token.TokenType;

import java.util.HashMap;
import java.util.Map;

public class BinaryOperationFactoryImpl implements BinaryOperationFactory {

    private final Map<String, BinaryOperation> operationMap = new HashMap<>();

    public BinaryOperationFactoryImpl() {
        init();
    }

    private void init() {
        operationMap.put(TokenType.EQ.name(), new EqOperation()); // ==
        operationMap.put(TokenType.NE.name(), new NeOperation()); // !=
        operationMap.put(TokenType.LT.name(), new LtOperation()); // <
        operationMap.put(TokenType.GT.name(), new GtOperation()); // >
        operationMap.put(TokenType.LE.name(), new LeOperation()); // <=
        operationMap.put(TokenType.GE.name(), new GeOperation()); // >=
        operationMap.put(TokenType.ADD.name(), new AddOperation()); // +
        operationMap.put(TokenType.SUB.name(), new SubOperation()); // -
        operationMap.put(TokenType.MUL.name(), new MulOperation()); // *
        operationMap.put(TokenType.DIV.name(), new DivOperation()); // /
        operationMap.put(TokenType.CALL.name(), new CallOperation()); // ()
        operationMap.put(TokenType.INDEX.name(), new IndexOperation()); // []
        operationMap.put(TokenType.IDENTIFIER.name(), new CustomOperation()); // custom

        // logic operation
        operationMap.put(TokenType.AND.name(), new AndOperation()); // &&
        operationMap.put(TokenType.OR.name(), new OrOperation()); // ||
        operationMap.put(TokenType.NOT.name(), new NotOperation()); // !
    }

    @Override
    public BinaryOperation findOperation(String type) {
        return operationMap.get(type);
    }

    @Override
    public void updateOperation(String type, BinaryOperation operation) {
        this.operationMap.put(type, operation);
    }
}
