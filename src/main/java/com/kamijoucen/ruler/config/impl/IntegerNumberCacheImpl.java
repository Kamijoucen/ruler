package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.IntegerNumberCache;
import com.kamijoucen.ruler.value.IntegerValue;

/**
 * 整数值缓存实现，用于减少小整数对象的创建
 * 缓存范围：[0, CACHE_SIZE)
 *
 * @author Kamijoucen
 */
public class IntegerNumberCacheImpl implements IntegerNumberCache {

    private static final int CACHE_SIZE = 1024;
    private static final IntegerValue[] CACHE;

    // 静态初始化块，确保线程安全
    static {
        CACHE = new IntegerValue[CACHE_SIZE];
        for (int i = 0; i < CACHE_SIZE; i++) {
            CACHE[i] = new IntegerValue(i);
        }
    }

    public IntegerNumberCacheImpl() {
        // 缓存已在静态块中初始化
    }

    /**
     * 获取整数值对象
     * 如果在缓存范围内则返回缓存的对象，否则创建新对象
     *
     * @param num 整数值
     * @return IntegerValue对象
     */
    @Override
    public IntegerValue getValue(long num) {
        if (num >= 0 && num < CACHE_SIZE) {
            return CACHE[(int) num];
        }
        return new IntegerValue(num);
    }
}
