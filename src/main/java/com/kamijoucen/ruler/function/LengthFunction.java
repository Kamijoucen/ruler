package com.kamijoucen.ruler.function;

import java.util.List;

public class LengthFunction implements RulerFunction {

    @Override
    public String getName() {
        return "length";
    }

    @Override
    public Object call(Object... param) {
        return ((List<?>) param[0]).size();
    }

}
