package com.kamijoucen.ruler.config.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kamijoucen.ruler.config.CustomImportLoaderManager;
import com.kamijoucen.ruler.config.option.CustomImportLoader;
import com.kamijoucen.ruler.config.option.ImportMatchOrder;
import com.kamijoucen.ruler.util.AssertUtil;

public class CustomImportLoaderManagerImpl implements CustomImportLoaderManager {

    private static final Logger log = LoggerFactory.getLogger(CustomImportLoaderManagerImpl.class);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    protected final List<SortLoader> loaders = new ArrayList<>();

    @Override
    public String load(String path) {
        lock.readLock().lock();
        try {
            for (SortLoader loader : loaders) {
                if (!loader.loader.match(path)) {
                    continue;
                }
                try {
                    return loader.loader.load(path);
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
    public void registerCustomImportLoader(CustomImportLoader customImportLoad) {
        AssertUtil.notNull(customImportLoad);
        ImportMatchOrder option = customImportLoad.getClass().getAnnotation(ImportMatchOrder.class);
        if (option == null) {
            option = CustomImportLoader.class.getAnnotation(ImportMatchOrder.class);
        }
        lock.writeLock().lock();
        try {
            loaders.add(new SortLoader(customImportLoad, option.order()));
            loaders.sort(SortLoader::compareTo);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeCustomImportLoader(CustomImportLoader customImportLoader) {
        AssertUtil.notNull(customImportLoader);
        lock.writeLock().lock();
        try {
            loaders.removeIf(loader -> loader.loader.equals(customImportLoader));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static class SortLoader implements Comparable<SortLoader> {

        private final CustomImportLoader loader;
        private final int order;

        public SortLoader(CustomImportLoader customImportLoader, int order) {
            this.loader = customImportLoader;
            this.order = order;
        }

        public CustomImportLoader getLoader() {
            return loader;
        }

        public int getOrder() {
            return order;
        }

        @Override
        public int compareTo(SortLoader o) {
            return Integer.compare(o.order, this.order);
        }
    }

}
