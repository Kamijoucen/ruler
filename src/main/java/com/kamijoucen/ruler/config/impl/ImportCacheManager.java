package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.util.IOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImportCacheManager {

    private final Map<String, RulerModule> cache = new ConcurrentHashMap<String, RulerModule>();

    public List<RulerModule> getAllImportModule() {
        return new ArrayList<>(cache.values());
    }

    public RulerModule getImportModule(String path) {
        if (IOUtil.isBlank(path)) {
            return null;
        }
        return cache.get(path);
    }

    public void putImportModule(String path, RulerModule module) {
        this.cache.put(path, module);
    }
}
