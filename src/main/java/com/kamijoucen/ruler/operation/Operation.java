package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.value.BaseValue;

public interface Operation {

    BaseValue compute(RuntimeContext context, BaseValue... param);

}
