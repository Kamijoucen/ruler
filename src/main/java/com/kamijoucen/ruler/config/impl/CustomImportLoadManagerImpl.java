package com.kamijoucen.ruler.config.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kamijoucen.ruler.config.CustomImportLoadManager;
import com.kamijoucen.ruler.config.option.CustomImportLoad;

public class CustomImportLoadManagerImpl implements CustomImportLoadManager {

    private static final Logger log = LoggerFactory.getLogger(CustomImportLoadManagerImpl.class);

    private final List<CustomImportLoad> customImportLoads = new ArrayList<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public String load(String path) {
        lock.readLock().lock();
        try {
            for (CustomImportLoad customImportLoad : customImportLoads) {
                if (!customImportLoad.match(path)) {
                    continue;
                }
                try {
                    return customImportLoad.load(path);
                } catch (Exception e) {
                    log.error("load custom import error, path: {}", path, e);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    @Override
    public void registerCustomImportLoad(CustomImportLoad customImportLoad) {
        lock.writeLock().lock();
        try {
            customImportLoads.add(customImportLoad);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeCustomImportLoad(CustomImportLoad customImportLoad) {
        lock.writeLock().lock();
        try {
            customImportLoads.remove(customImportLoad);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int customCount() {
        lock.readLock().lock();
        try {
            return customImportLoads.size();
        } finally {
            lock.readLock().unlock();
        }
    }

}
