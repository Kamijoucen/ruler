package com.kamijoucen.ruler.config.impl;

import com.kamijoucen.ruler.config.ConfigModuleManager;
import com.kamijoucen.ruler.config.option.ConfigModule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConfigModuleManagerImpl implements ConfigModuleManager {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Map<String, ConfigModule> modules = new HashMap<>();

    private final Set<String> aliasSet = new HashSet<>();

    @Override
    public ConfigModule findModule(String path) {
        lock.readLock().lock();
        try {
            return modules.get(path);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void removeModule(String path) {
        lock.writeLock().lock();
        try {
            ConfigModule module = modules.remove(path);
            if (module != null && module.getAlias() != null) {
                aliasSet.remove(module.getAlias());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void registerModule(ConfigModule module, boolean overrideAlias) {
        lock.writeLock().lock();
        try {
            if (overrideAlias) {
                modules.put(module.getUri(), module);
                if (module.getAlias() != null) {
                    aliasSet.add(module.getAlias());
                }
            } else {
                if (module.getAlias() != null && aliasSet.contains(module.getAlias())) {
                    throw new IllegalArgumentException("alias " + module.getAlias() + " already exists");
                }
                modules.put(module.getUri(), module);
                if (module.getAlias() != null) {
                    aliasSet.add(module.getAlias());
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
