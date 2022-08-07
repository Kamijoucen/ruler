package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.IntegerNumberCache;
import com.kamijoucen.ruler.value.IntegerValue;

public class IntegerNumberCacheImpl implements IntegerNumberCache {

    private static final int CACHE_SIZE = 1024;
    private static final IntegerValue[] CACHE = new IntegerValue[CACHE_SIZE];

    public IntegerNumberCacheImpl() {
        init();
    }

    @Override
    public IntegerValue getValue(long num) {
        if (num >= 0 && num < CACHE_SIZE) {
            return CACHE[(int) num];
        }
        return new IntegerValue(num);
    }

    private void init() {
        for (int i = 0; i < CACHE_SIZE; i++) {
            CACHE[i] = new IntegerValue(i);
        }
    }

}
