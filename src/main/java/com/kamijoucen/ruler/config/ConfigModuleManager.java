package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.config.option.ConfigModule;

public interface ConfigModuleManager {

    ConfigModule findModule(String path);

    void removeModule(String path);

    void registerModule(ConfigModule module);

}
