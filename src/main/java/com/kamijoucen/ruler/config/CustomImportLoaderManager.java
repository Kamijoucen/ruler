package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.config.option.CustomImportLoader;

public interface CustomImportLoaderManager {

    void registerCustomImportLoader(CustomImportLoader loader);

    void removeCustomImportLoader(CustomImportLoader loader);

    String load(String path);

    int customCount();

}
