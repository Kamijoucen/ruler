package com.kamijoucen.ruler.types.spi;

@ImportMatchOrder
public interface CustomImportLoader {

    boolean match(String path);

    String load(String path);

}
