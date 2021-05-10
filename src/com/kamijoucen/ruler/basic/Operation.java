package com.kamijoucen.ruler.basic;

import com.kamijoucen.ruler.value.BaseValue;

public interface Operation {

    BaseValue eval(BaseValue... param);

}
