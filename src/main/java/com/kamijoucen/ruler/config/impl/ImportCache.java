package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.module.RulerModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImportCache {

    private Map<String, RulerModule> cache = new ConcurrentHashMap<String, RulerModule>();

    public List<RulerModule> getAllImportModule() {
        return new ArrayList<RulerModule>(cache.values());
    }

    public RulerModule getImportModule(String path) {
        return cache.get(path);
    }

    public void putImportModule(String path, RulerModule module) {
        this.cache.put(path, module);
    }
}
