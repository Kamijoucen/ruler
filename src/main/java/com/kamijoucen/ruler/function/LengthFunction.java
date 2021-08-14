package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.exception.SyntaxException;

import java.util.List;

public class LengthFunction implements RulerFunction {

    @Override
    public String getName() {
        return "length";
    }

    @Override
    public Object call(Object... param) {
        if (param == null || param.length != 1) {
            throw SyntaxException.withSyntax("length只接收一个参数");
        }
        if (!(param[0] instanceof List)) {
            return 1;
        }
        return ((List<?>) param[0]).size();
    }

}
