package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.component.option.CustomImportLoader;

public interface CustomImportLoaderManager {

    void registerCustomImportLoader(CustomImportLoader loader);

    void removeCustomImportLoader(CustomImportLoader loader);

    String load(String path);

}
