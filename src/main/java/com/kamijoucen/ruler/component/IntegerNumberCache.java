package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.value.IntegerValue;

import java.math.BigInteger;

public interface IntegerNumberCache {

    IntegerValue getValue(BigInteger num);

}
