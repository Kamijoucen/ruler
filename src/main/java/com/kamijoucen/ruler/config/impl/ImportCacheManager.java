package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.util.IOUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 导入模块缓存管理器
 * 用于缓存已编译的模块，避免重复编译
 * 线程安全的实现
 *
 * @author Kamijoucen
 */
public class ImportCacheManager {

    private final Map<String, RulerModule> cache = new ConcurrentHashMap<>();

    /**
     * 获取所有已缓存的导入模块
     *
     * @return 所有模块的列表（只读）
     */
    public List<RulerModule> getAllImportModule() {
        return Collections.unmodifiableList(new ArrayList<>(cache.values()));
    }

    /**
     * 根据路径获取缓存的模块
     *
     * @param path 模块路径
     * @return 缓存的模块，如果不存在或路径为空则返回null
     */
    public RulerModule getImportModule(String path) {
        if (IOUtil.isBlank(path)) {
            return null;
        }
        return cache.get(path);
    }

    /**
     * 缓存导入的模块
     *
     * @param path 模块路径
     * @param module 要缓存的模块
     * @throws NullPointerException 如果path或module为null
     */
    public void putImportModule(String path, RulerModule module) {
        Objects.requireNonNull(path, "Module path cannot be null");
        Objects.requireNonNull(module, "Module cannot be null");

        if (IOUtil.isBlank(path)) {
            throw new IllegalArgumentException("Module path cannot be blank");
        }

        cache.put(path, module);
    }

    /**
     * 清空所有缓存
     */
    public void clear() {
        cache.clear();
    }

    /**
     * 移除指定路径的缓存模块
     *
     * @param path 模块路径
     * @return 被移除的模块，如果不存在则返回null
     */
    public RulerModule removeImportModule(String path) {
        if (IOUtil.isBlank(path)) {
            return null;
        }
        return cache.remove(path);
    }

    /**
     * 获取缓存的模块数量
     *
     * @return 缓存的模块数量
     */
    public int size() {
        return cache.size();
    }
}
