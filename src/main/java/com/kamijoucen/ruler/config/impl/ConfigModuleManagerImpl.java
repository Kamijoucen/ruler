package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.ConfigModuleManager;
import com.kamijoucen.ruler.config.option.ConfigModule;
import com.kamijoucen.ruler.util.IOUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigModuleManagerImpl implements ConfigModuleManager {

    private final Map<String, ConfigModule> modules = new ConcurrentHashMap<>();

    @Override
    public ConfigModule findModule(String path) {
        return modules.get(path);
    }

    @Override
    public void removeModule(String path) {
        modules.remove(path);
    }

    @Override
    public void registerModule(ConfigModule module) {
        if (IOUtil.isBlank(module.getUri())) {
            throw new IllegalArgumentException("module uri can not be blank");
        }
        modules.put(module.getUri(), module);
    }
}
