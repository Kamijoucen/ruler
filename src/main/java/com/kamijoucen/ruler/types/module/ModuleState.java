package com.kamijoucen.ruler.types.module;

import com.kamijoucen.ruler.types.spi.CustomImportLoader;
import com.kamijoucen.ruler.types.spi.ImportMatchOrder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/** Module definitions, compiled modules and host loaders owned by one engine. */
public final class ModuleState {

    private final Map<String, RulerModule> compiledModules = new ConcurrentHashMap<>();
    private final Map<String, ConfigModule> registeredModules = new ConcurrentHashMap<>();
    private final List<CustomImportLoader> loaders = new ArrayList<>();

    public RulerModule findCached(String path) {
        return path == null || path.trim().isEmpty() ? null : compiledModules.get(path);
    }

    public void cache(String path, RulerModule module) {
        compiledModules.put(path, module);
    }

    public List<RulerModule> getCachedModules() {
        return new ArrayList<>(compiledModules.values());
    }

    public ConfigModule findRegistered(String path) {
        return registeredModules.get(path);
    }

    public void register(ConfigModule module) {
        Objects.requireNonNull(module, "module");
        if (module.getUri() == null || module.getUri().trim().isEmpty()) {
            throw new IllegalArgumentException("module uri cannot be blank");
        }
        registeredModules.put(module.getUri(), module);
    }

    public void remove(String path) {
        registeredModules.remove(path);
    }

    public synchronized List<CustomImportLoader> getLoaders() {
        return new ArrayList<>(loaders);
    }

    public synchronized void registerLoader(CustomImportLoader loader) {
        loaders.add(Objects.requireNonNull(loader, "loader"));
        loaders.sort(Comparator.comparingInt(ModuleState::loaderOrder).reversed());
    }

    public synchronized void removeLoader(CustomImportLoader loader) {
        Objects.requireNonNull(loader, "loader");
        loaders.removeIf(registered -> registered.equals(loader));
    }

    private static int loaderOrder(CustomImportLoader loader) {
        ImportMatchOrder option = loader.getClass().getAnnotation(ImportMatchOrder.class);
        if (option == null) {
            option = CustomImportLoader.class.getAnnotation(ImportMatchOrder.class);
        }
        return option.order();
    }
}
