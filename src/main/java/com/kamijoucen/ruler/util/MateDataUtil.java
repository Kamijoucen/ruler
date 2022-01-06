package com.kamijoucen.ruler.util;

import com.kamijoucen.ruler.function.RulerFunction;
import com.kamijoucen.ruler.function.ValueConvertFunctionProxy;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.FunctionValue;

public class MateDataUtil {

    public static void initArrayMateData(final ArrayValue value) {
        FunctionValue arrSizeFun = new FunctionValue(new ValueConvertFunctionProxy(new RulerFunction() {
            private final ArrayValue array = value;

            @Override
            public String getName() {
                return "size";
            }

            @Override
            public Object call(Object... param) {
                return array.getValues().size();
            }
        }));
        value.getClassInfo().put(arrSizeFun.getValue().getName(), arrSizeFun);
    }
}
