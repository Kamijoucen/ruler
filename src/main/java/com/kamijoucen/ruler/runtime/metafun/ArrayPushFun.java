package com.kamijoucen.ruler.runtime.metafun;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.ArrayValue;
import com.kamijoucen.ruler.value.BaseValue;
import com.kamijoucen.ruler.value.constant.NullValue;

public class ArrayPushFun extends AbstractMetaFun {

    @Override
    public String getName() {
        return "push";
    }

    @Override
    public Object call(RuntimeContext context, BaseValue self, Object... param) {
        if (param == null || param.length == 0) {
            return NullValue.INSTANCE;
        }
        ArrayValue source = (ArrayValue) this.getSource();
        source.getValues().add((BaseValue) param[0]);
        return NullValue.INSTANCE;
    }

}
