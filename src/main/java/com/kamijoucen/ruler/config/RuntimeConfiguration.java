package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.module.RulerModule;

import java.util.List;

public interface RuntimeConfiguration {

    List<RulerModule> getAllImportModuleCache();

    RulerModule getImportModuleCache(String path);

    void putImportModuleCache(String path, RulerModule module);

}
