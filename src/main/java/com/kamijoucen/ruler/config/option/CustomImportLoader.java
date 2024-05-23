package com.kamijoucen.ruler.config.option;

@ImportMatchOrder
public interface CustomImportLoader {

    boolean match(String path);

    String load(String path);

}
