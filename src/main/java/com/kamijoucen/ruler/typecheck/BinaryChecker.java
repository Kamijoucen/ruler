package com.kamijoucen.ruler.typecheck;

import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.common.BaseEval;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.type.BoolType;
import com.kamijoucen.ruler.type.DoubleType;
import com.kamijoucen.ruler.type.FailureType;
import com.kamijoucen.ruler.type.IntegerType;
import com.kamijoucen.ruler.type.StringType;
import com.kamijoucen.ruler.type.UnknowType;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.ValueType;

public class BinaryChecker implements BaseEval<BinaryOperationNode> {

    private static final BaseValue[][] typeBaseMap;
    private static final BaseValue[][] typeAddMap;

    static {
        typeBaseMap = new BaseValue[ValueType.values().length][ValueType.values().length];
        typeAddMap = new BaseValue[ValueType.values().length][ValueType.values().length];
        initBaseMap();
        initAddMap();
    }

    private static void initBaseMap() {
        typeBaseMap[ValueType.INTEGER.ordinal()][ValueType.INTEGER.ordinal()] = IntegerType.INSTANCE;
        typeBaseMap[ValueType.INTEGER.ordinal()][ValueType.DOUBLE.ordinal()] = DoubleType.INSTANCE;
        typeBaseMap[ValueType.INTEGER.ordinal()][ValueType.STRING.ordinal()] = UnknowType.INSTANCE;

        typeBaseMap[ValueType.DOUBLE.ordinal()][ValueType.INTEGER.ordinal()] = DoubleType.INSTANCE;
        typeBaseMap[ValueType.DOUBLE.ordinal()][ValueType.DOUBLE.ordinal()] = DoubleType.INSTANCE;
        typeBaseMap[ValueType.DOUBLE.ordinal()][ValueType.STRING.ordinal()] = UnknowType.INSTANCE;

        typeBaseMap[ValueType.STRING.ordinal()][ValueType.INTEGER.ordinal()] = UnknowType.INSTANCE;
        typeBaseMap[ValueType.STRING.ordinal()][ValueType.DOUBLE.ordinal()] = UnknowType.INSTANCE;
    }

    private static void initAddMap() {
        typeAddMap[ValueType.INTEGER.ordinal()][ValueType.INTEGER.ordinal()] = IntegerType.INSTANCE;
        typeAddMap[ValueType.INTEGER.ordinal()][ValueType.DOUBLE.ordinal()] = DoubleType.INSTANCE;
        typeAddMap[ValueType.INTEGER.ordinal()][ValueType.STRING.ordinal()] = UnknowType.INSTANCE;

        typeAddMap[ValueType.DOUBLE.ordinal()][ValueType.INTEGER.ordinal()] = DoubleType.INSTANCE;
        typeAddMap[ValueType.DOUBLE.ordinal()][ValueType.DOUBLE.ordinal()] = DoubleType.INSTANCE;
        typeAddMap[ValueType.DOUBLE.ordinal()][ValueType.STRING.ordinal()] = UnknowType.INSTANCE;

        typeAddMap[ValueType.STRING.ordinal()][ValueType.STRING.ordinal()] = StringType.INSTANCE;
        typeAddMap[ValueType.STRING.ordinal()][ValueType.INTEGER.ordinal()] = UnknowType.INSTANCE;
        typeAddMap[ValueType.STRING.ordinal()][ValueType.DOUBLE.ordinal()] = UnknowType.INSTANCE;
    }

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        BaseValue val1 = node.getExp1().typeCheck(context, scope);
        BaseValue val2 = node.getExp2().typeCheck(context, scope);
        if (val1.getType() == ValueType.FAILURE || val2.getType() == ValueType.FAILURE) {
            return FailureType.INSTANCE;
        }
        TokenType op = node.getOp();
        if (op == TokenType.EQ || op == TokenType.NE) {
            return BoolType.INSTANCE;
        }
        if (val1.getType() == ValueType.UN_KNOW || val2.getType() == ValueType.UN_KNOW) {
            return UnknowType.INSTANCE;
        }
        BaseValue typeValue = null;
        if (op == TokenType.ADD) {
            typeValue = typeAddMap[val1.getType().ordinal()][val2.getType().ordinal()];
        } else if (op == TokenType.SUB) {
            typeValue = typeBaseMap[val1.getType().ordinal()][val2.getType().ordinal()];
        } else if (op == TokenType.MUL || op == TokenType.DIV) {
            typeValue = typeBaseMap[val1.getType().ordinal()][val2.getType().ordinal()];
        }
        if (typeValue == null) {
            return UnknowType.INSTANCE;
        }
        return typeValue;
    }

}