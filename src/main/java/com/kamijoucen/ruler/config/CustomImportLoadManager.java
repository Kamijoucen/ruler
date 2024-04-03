package com.kamijoucen.ruler.config;

import com.kamijoucen.ruler.config.option.CustomImportLoad;

public interface CustomImportLoadManager {

    void registerCustomImportLoad(CustomImportLoad customImportLoad);

    void removeCustomImportLoad(CustomImportLoad customImportLoad);

    String load(String path);

    int customCount();

}
