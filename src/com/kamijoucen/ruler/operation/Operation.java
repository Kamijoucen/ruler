package com.kamijoucen.ruler.operation;

import com.kamijoucen.ruler.value.BaseValue;

public interface Operation {

    BaseValue eval(BaseValue... param);

}
