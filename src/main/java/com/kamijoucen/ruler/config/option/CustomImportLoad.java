package com.kamijoucen.ruler.config.option;

public interface CustomImportLoad {

    boolean match(String path);

    String load(String path);

}
