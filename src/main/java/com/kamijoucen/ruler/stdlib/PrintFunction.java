package com.kamijoucen.ruler.stdlib;
import com.kamijoucen.ruler.types.spi.RulerFunction;

import com.kamijoucen.ruler.types.runtime.RuntimeContext;
import com.kamijoucen.ruler.types.runtime.Scope;
import com.kamijoucen.ruler.types.value.BaseValue;

import java.util.Arrays;

public class PrintFunction implements RulerFunction {

    @Override
    public String getName() {
        return "println";
    }

    @Override
    public Object call(RuntimeContext context, Scope currentScope, BaseValue self, Object... param) {
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
