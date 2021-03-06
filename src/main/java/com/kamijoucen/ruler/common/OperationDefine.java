package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.*;
import com.kamijoucen.ruler.token.TokenType;

import java.util.HashMap;
import java.util.Map;

public class OperationDefine {

    private static final Map<TokenType, Integer> PRECEDENCE = new HashMap<TokenType, Integer>();
    private static final Map<TokenType, Operation> OPERATION = new HashMap<TokenType, Operation>();
    private static final Map<TokenType, LogicOperation> LOGIC_OPERATION = new HashMap<TokenType, LogicOperation>();
    private static final Map<TokenType, AssignOperation> ASSIGN_OPERATION = new HashMap<TokenType, AssignOperation>();

    static {

        PRECEDENCE.put(TokenType.OR, 8); // ||
        PRECEDENCE.put(TokenType.AND, 9); // &&
        PRECEDENCE.put(TokenType.EQ, 10); // ==
        PRECEDENCE.put(TokenType.NE, 10); // !=
        PRECEDENCE.put(TokenType.LT, 20); // <
        PRECEDENCE.put(TokenType.GT, 20); // >
        PRECEDENCE.put(TokenType.LE, 20); // <=
        PRECEDENCE.put(TokenType.GE, 20); // >=
        PRECEDENCE.put(TokenType.ADD, 30); // +
        PRECEDENCE.put(TokenType.SUB, 30); // -
        PRECEDENCE.put(TokenType.MUL, 40); // *
        PRECEDENCE.put(TokenType.DIV, 40); // /

        OPERATION.put(TokenType.EQ, new EqOperation()); // ==
        OPERATION.put(TokenType.NE, new NeOperation()); // !=
        OPERATION.put(TokenType.LT, new LtOperation()); // <
        OPERATION.put(TokenType.GT, new GtOperation()); // >
        OPERATION.put(TokenType.LE, new LeOperation()); // <=
        OPERATION.put(TokenType.GE, new GeOperation()); // >=
        OPERATION.put(TokenType.ADD, new AddOperation()); // +
        OPERATION.put(TokenType.SUB, new SubOperation()); // -
        OPERATION.put(TokenType.MUL, new MulOperation()); // *
        OPERATION.put(TokenType.DIV, new DivOperation()); // /
        OPERATION.put(TokenType.CALL, new CallOperation()); // ()
        OPERATION.put(TokenType.INDEX, new IndexOperation()); // []

        LOGIC_OPERATION.put(TokenType.AND, new AndOperation()); // &&
        LOGIC_OPERATION.put(TokenType.OR, new OrOperation()); // ||
        LOGIC_OPERATION.put(TokenType.NOT, new NotOperation()); // !

        ASSIGN_OPERATION.put(TokenType.INDEX, new IndexAssignOperation()); // []
        ASSIGN_OPERATION.put(TokenType.DOT, new DotAssignOperation()); // .
    }

    public static LogicOperation findLogicOperation(TokenType type) {
        LogicOperation operation = LOGIC_OPERATION.get(type);
        if (operation == null) {
            throw SyntaxException.withSyntax("???????????????????????????:" + type);
        }
        return operation;
    }

    public static Operation findOperation(TokenType type) {
        Operation operation = OPERATION.get(type);
        if (operation == null) {
            throw SyntaxException.withSyntax("?????????????????????:" + type);
        }
        return operation;
    }

    public static AssignOperation findAssignOperation(TokenType type) {
        AssignOperation operation = ASSIGN_OPERATION.get(type);
        if (operation == null) {
            throw SyntaxException.withSyntax("????????????????????????:" + type);
        }
        return operation;
    }

    public static int findPrecedence(TokenType type) {
        Integer precedence = PRECEDENCE.get(type);
        if (precedence == null) {
            return -1;
        } else {
            return precedence;
        }
    }
}
