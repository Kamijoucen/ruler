package com.kamijoucen.ruler.function;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.Arrays;

public class PrintFunction implements RulerFunction {

    @Override
    public String getName() {
        return "println";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            System.out.println();
        } else if (param.length == 1) {
            System.out.println(param[0]);
        } else {
            System.out.println(Arrays.toString(param));
        }
        return null;
    }
}
