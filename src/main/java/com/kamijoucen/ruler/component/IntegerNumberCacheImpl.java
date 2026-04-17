package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.value.IntegerValue;

import java.math.BigInteger;

public class IntegerNumberCacheImpl implements IntegerNumberCache {

    private static final int CACHE_SIZE = 1024;
    private static final IntegerValue[] CACHE = new IntegerValue[CACHE_SIZE];

    public IntegerNumberCacheImpl() {
        init();
    }

    @Override
    public IntegerValue getValue(BigInteger num) {
        if (num.signum() >= 0 && num.compareTo(BigInteger.valueOf(CACHE_SIZE)) < 0) {
            int index = num.intValue();
            return CACHE[index];
        }
        return new IntegerValue(num);
    }

    private void init() {
        for (int i = 0; i < CACHE_SIZE; i++) {
            CACHE[i] = new IntegerValue(BigInteger.valueOf(i));
        }
    }

}
