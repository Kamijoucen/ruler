package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.ConfigModule;

public interface ConfigModuleManager {

    ConfigModule findModule(String path);

    void removeModule(String path);

    void registerModule(ConfigModule module);

}
