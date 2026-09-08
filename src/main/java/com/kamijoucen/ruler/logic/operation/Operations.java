package com.kamijoucen.ruler.logic.operation;

import com.kamijoucen.ruler.types.token.TokenType;
import java.util.HashMap;
import java.util.Map;

/** Fixed language operators; user-defined infix functions live in RuntimeContext. */
public final class Operations {
    private static final Map<String, BinaryOperation> OPERATIONS = createOperations();
    private Operations() {}
    private static Map<String, BinaryOperation> createOperations() {
        Map<String, BinaryOperation> operationMap = new HashMap<>();
        operationMap.put(TokenType.EQ.name(), new EqOperation(false)); // ==
        operationMap.put(TokenType.STRICT_EQ.name(), new EqOperation(true)); // ===
        operationMap.put(TokenType.NE.name(), new NeOperation(false)); // !=
        operationMap.put(TokenType.STRICT_NE.name(), new NeOperation(true)); // !==
        operationMap.put(TokenType.LT.name(), new LtOperation()); // <
        operationMap.put(TokenType.GT.name(), new GtOperation()); // >
        operationMap.put(TokenType.LE.name(), new LeOperation()); // <=
        operationMap.put(TokenType.GE.name(), new GeOperation()); // >=
        operationMap.put(TokenType.ADD.name(), new AddOperation()); // +
        operationMap.put(TokenType.STRING_ADD.name(), new StringAddOperation()); // ++
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
        return Map.copyOf(operationMap);
    }
    public static BinaryOperation findOperation(String name) { return OPERATIONS.get(name); }
}
