package com.kamijoucen.ruler.runtime.metafun;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.ArrayValue;

public class ArraySizeFun extends AbstractMetaFun {

    @Override
    public String getName() {
        return "size";
    }

    @Override
    public Object call(RuntimeContext context, Object... param) {
        ArrayValue source = (ArrayValue) this.getSource();
        return context.getConfiguration().getIntegerNumberCache().getValue(source.getValues().size());
    }

}
